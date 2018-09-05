package studio.vifi.hknews.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import android.support.annotation.WorkerThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import studio.vifi.hknews.data.api.ApiService
import studio.vifi.hknews.data.model.Item
import studio.vifi.hknews.data.model.StoryType
import timber.log.Timber
import java.util.concurrent.Executor

class ItemKeyedDataSource(
        private val api: ApiService,
        private val requestType: StoryType,
        private val networkExecutor: Executor)
    : ItemKeyedDataSource<Long, Item>() {

    private val itemIds: MutableList<Long> = mutableListOf()

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter and we don't support loadBefore
     * in this example.
     * <p>
     * See BoundaryCallback example for a more complete example on syncing multiple network states.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            networkExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
            params: LoadInitialParams<Long>,
            callback: LoadInitialCallback<Item>) {

        initialLoad.postValue(NetworkState.loading())

        if (itemIds.isEmpty()) {
            api.getItems(requestType.requestPath()).enqueue(object : Callback<List<Long>> {
                override fun onFailure(call: Call<List<Long>>, t: Throwable) {
                    Timber.w(t)
                    retry = {
                        loadInitial(params, callback)
                    }
                    val error = NetworkState.failed(t.message ?: "unknown error")
                    initialLoad.postValue(error)
                }

                override fun onResponse(call: Call<List<Long>>, response: Response<List<Long>>) {
                    val itemIds = response.body()
                    Timber.d("Request to ${requestType.name} returns ${itemIds?.size ?: 0}")
                    networkExecutor.execute {
                        val items = itemIds?.let {
                            this@ItemKeyedDataSource.itemIds.addAll(it)

                            loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                        } ?: emptyList()

                        initialLoad.postValue(NetworkState.loaded())
                        postResult(items, callback)
                    }
                }
            })
        } else {
            networkExecutor.execute {
                val items = loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                initialLoad.postValue(NetworkState.loaded())
                postResult(items, callback)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Item>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Item>) {
        networkState.postValue(NetworkState.loading())

        if (itemIds.isEmpty()) {
            api.getItems(requestType.requestPath()).enqueue(object : Callback<List<Long>> {
                override fun onFailure(call: Call<List<Long>>, t: Throwable) {
                    Timber.w(t)
                    retry = {
                        loadAfter(params, callback)
                    }
                    val error = NetworkState.failed(t.message ?: "unknown error")
                    networkState.postValue(error)
                }

                override fun onResponse(call: Call<List<Long>>, response: Response<List<Long>>) {
                    val itemIds = response.body()
                    Timber.d("Request to ${requestType.name} returns ${itemIds?.size ?: 0}")
                    networkExecutor.execute {
                        val items = itemIds?.let {
                            this@ItemKeyedDataSource.itemIds.addAll(it)

                            loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                        } ?: emptyList()

                        postResult(items, callback)
                    }
                }
            })
        } else {
            networkExecutor.execute {
                val items = loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                postResult(items, callback)
            }
        }
    }

    override fun getKey(item: Item): Long = item.id

    private fun postResult(items: List<Item>, callback: LoadCallback<Item>) {
        retry = null
        callback.onResult(items)
        networkState.postValue(NetworkState.loaded())
    }

    @WorkerThread
    private fun loadItems(requestItemIds: List<Long>,
                          after: Long? = null,
                          loadSize: Int): List<Item> {
        return requestItemIds
                .let { ids ->
                    val size = ids.size

                    val startIndex = if (after == null) {
                        0
                    } else {
                        ids.indexOf(after)
                    }

                    val endIndex = if (size < startIndex + loadSize) {
                        size - 1
                    } else {
                        startIndex + loadSize
                    }

                    if (startIndex == -1) {
                        Timber.e("No item found for $after")
                        emptyList()
                    } else {
                        ids.subList(startIndex, endIndex)
                    }
                }
                .mapNotNull {
                    val itemDetailResponse = api.getItemDetail(it).execute()
                    return@mapNotNull if (itemDetailResponse.isSuccessful) {
                        itemDetailResponse.body()
                    } else {
                        Timber.e("Request failed for $it with ${itemDetailResponse.message()}")
                        null
                    }
                }
    }

//    override fun loadKidIds(): Single<List<Long>> {
//        return if (itemIds.isEmpty()) {
//            api.getItemDetail(itemId)
//                    .map { it.kids }
//                    .map {
//                        itemIds = it
//                        itemIds
//                    }
//        } else {
//            Single.just(itemIds)
//        }
}