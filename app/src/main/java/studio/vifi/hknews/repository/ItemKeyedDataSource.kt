package studio.vifi.hknews.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import studio.vifi.hknews.SchedulerProvider
import studio.vifi.hknews.api.ApiService
import studio.vifi.hknews.vo.Item

abstract class ItemKeyedDataSource(
        protected val api: ApiService,
        private val schedulerProvider: SchedulerProvider)
    : ItemKeyedDataSource<Long, Item>() {

    val liveSource: MutableLiveData<studio.vifi.hknews.repository.ItemKeyedDataSource> = MutableLiveData()

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter and we don't support loadBefore
     * in this example.
     * <p>
     * See BoundaryCallback example for a more complete example on syncing multiple network states.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Item>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Item>) {
        loadItemDetails(after = params.key,
                loadSize = params.requestedLoadSize,
                loadCallback = callback,
                retry = { })
    }

    override fun getKey(item: Item): Long = item.id

    override fun loadInitial(
            params: LoadInitialParams<Long>,
            callback: LoadInitialCallback<Item>) {

        loadItemDetails(loadSize = params.requestedLoadSize,
                loadCallback = callback,
                retry = { loadInitial(params, callback) })
    }

    private fun loadItemDetails(after: Long? = null, loadSize: Int, loadCallback: LoadCallback<Item>, retry: () -> Any) {
        loadKidIds()
                .flatMap { ids ->
                    val startIndex = ids.indexOf(after ?: ids[0])
                    ids
                            .subList(startIndex, startIndex + loadSize)
                            .map { id ->
                                api
                                        .getItemDetail(id)
                                        .doOnError { Log.d(TAG, it.message) }
                                        .onErrorReturnItem(Item.createEmpty())
                                        .map { item: Item -> listOf(item) }
                            }
                            .reduce { acc, single ->
                                acc.zipWith(single) { first, second -> first + second }
                            }
                }
                .doOnSubscribe {
                    // update network states.
                    // we also provide an initial load state to the listeners so that the UI can know when the
                    // very first list is loaded.
                    networkState.postValue(NetworkState.loading())
                    initialLoad.postValue(NetworkState.loading())
                }
                .doOnSuccess { items: List<Item> ->
                    networkState.postValue(NetworkState.loaded())
                    initialLoad.postValue(NetworkState.loaded())
                    loadCallback.onResult(items.filter { it.valid })
                }
                .onErrorReturn { emptyList() }
                .doOnError { throwable ->
                    retry()
                    val error = NetworkState.failed(throwable.message ?: "unknown error")
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe()

    }

    protected abstract fun loadKidIds(): Single<List<Long>>

    companion object {
        const val TAG = "DataSource"
    }
}