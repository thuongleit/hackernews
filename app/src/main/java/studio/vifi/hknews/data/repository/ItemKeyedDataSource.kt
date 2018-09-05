package studio.vifi.hknews.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import android.support.annotation.MainThread
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

    val networkState = MutableLiveData<NetworkState>()

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    private var currentNetworkType: RequestType? = null

    @MainThread
    fun refresh() {
        currentNetworkType = RequestType.REFRESH
        itemIds.clear()
        invalidate()
    }

    fun retry() {
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

        if (!isRefreshing()) {
            currentNetworkType = RequestType.INITIAL_LOAD
        }

        networkState.postValue(LOADING(currentNetworkType!!))
        if (itemIds.isEmpty()) {
            api.getItems(requestType.requestPath()).enqueue(object : Callback<List<Long>> {
                override fun onFailure(call: Call<List<Long>>, t: Throwable) {
                    Timber.w(t)
                    retry = {
                        loadInitial(params, callback)
                    }
                    networkState.postValue(ERROR(currentNetworkType!!, t))
                }

                override fun onResponse(call: Call<List<Long>>, response: Response<List<Long>>) {
                    val itemIds = response.body()
                    Timber.d("Request to ${requestType.name} returns ${itemIds?.size ?: 0}")
                    networkExecutor.execute {
                        val items = itemIds?.let {
                            this@ItemKeyedDataSource.itemIds.addAll(it)

                            loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                        } ?: emptyList()

                        networkState.postValue(LOADED(currentNetworkType!!))
                        postResult(items, callback)
                    }
                }
            })
        } else {
            networkExecutor.execute {
                networkState.postValue(LOADED(currentNetworkType!!))
                val items = loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                postResult(items, callback)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Item>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Item>) {
        currentNetworkType = RequestType.LOAD_MORE
        networkState.postValue(LOADING(currentNetworkType!!))

        if (itemIds.isEmpty()) {
            api.getItems(requestType.requestPath()).enqueue(object : Callback<List<Long>> {
                override fun onFailure(call: Call<List<Long>>, t: Throwable) {
                    Timber.w(t)
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(ERROR(currentNetworkType!!, t))
                }

                override fun onResponse(call: Call<List<Long>>, response: Response<List<Long>>) {
                    val itemIds = response.body()
                    Timber.d("Request to ${requestType.name} returns ${itemIds?.size ?: 0}")
                    networkExecutor.execute {
                        val items = itemIds?.let {
                            this@ItemKeyedDataSource.itemIds.addAll(it)

                            loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                        } ?: emptyList()

                        networkState.postValue(LOADED(currentNetworkType!!))
                        postResult(items, callback)
                    }
                }
            })
        } else {
            networkExecutor.execute {
                networkState.postValue(LOADED(RequestType.LOAD_MORE))
                val items = loadItems(requestItemIds = itemIds, loadSize = params.requestedLoadSize)
                postResult(items, callback)
            }
        }
    }

    override fun getKey(item: Item): Long = item.id

    private fun postResult(items: List<Item>, callback: LoadCallback<Item>) {
        retry = null
        callback.onResult(items)
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

    fun isRefreshing(): Boolean {
        return currentNetworkType == RequestType.REFRESH
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