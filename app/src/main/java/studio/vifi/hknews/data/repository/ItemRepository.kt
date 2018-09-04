package studio.vifi.hknews.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import studio.vifi.hknews.AppExecutors
import studio.vifi.hknews.data.api.ApiService
import studio.vifi.hknews.data.api.NetworkState
import studio.vifi.hknews.data.api.path
import studio.vifi.hknews.data.db.HackerNewsDb
import studio.vifi.hknews.data.vo.Item
import studio.vifi.hknews.data.worker.StoriesBoundaryCallback
import javax.inject.Inject

class ItemRepository @Inject constructor(
        private val apiService: ApiService,
        private val database: HackerNewsDb,
        private val appExecutors: AppExecutors
) {
    fun fetchStories(type: Item.StoryType, requestPageSize: Int = DEFAULT_LOCAL_PAGE_SIZE): Listing<Item> {
        val itemDataSource = database.itemDao().loadByType(type)

        val pagedConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(requestPageSize)
                .setPageSize(requestPageSize * 2)
                .build()

        val boundaryCallback = StoriesBoundaryCallback(
                type,
                apiService,
                appExecutors.networkIO(),
                DEFAULT_NETWORK_PAGE_SIZE
        ) { items: List<Item> ->
            database.itemDao().insert(*items.toTypedArray())
        }

        val pagedList = LivePagedListBuilder<Int, Item>(itemDataSource, pagedConfig)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(type, requestPageSize)
        }

        return Listing(
                pagedList = pagedList,
                networkState = boundaryCallback.networkState,
                retry = {},
                refresh = {
                    refreshTrigger.value = null
                },
                refreshState = refreshState
        )
    }

    private fun refresh(type: Item.StoryType, requestPageSize: Int): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.loading()

        apiService.getItems(type.path()).enqueue(
                object : Callback<List<Long>> {
                    override fun onResponse(call: Call<List<Long>>, response: Response<List<Long>>) {
                        appExecutors.diskIO().execute {
                            val latestItemId = database.itemDao().lastestItemId()

                            val items = response.body()
                                    ?.subList(0, requestPageSize)
                                    ?.filterNot { it > latestItemId }
                                    ?.mapNotNull { id ->
                                        val itemDetailResponse = apiService.getItemDetail(id).execute()

                                        return@mapNotNull if (itemDetailResponse.isSuccessful) {
                                            itemDetailResponse.body()
                                        } else {
                                            null
                                        }
                                    }?.map {
                                        it.copy(storyType = type)
                                    }
                                    ?.toList()

                            if (items != null) {
                                database.itemDao().insert(*items.toTypedArray())
                            }
                            networkState.postValue(NetworkState.loaded())
                        }
                    }

                    override fun onFailure(call: Call<List<Long>>, t: Throwable) {
                        networkState.value = NetworkState.failed(t.message)
                    }
                }
        )

        return networkState
    }

//    fun fetchDirectKids(itemId: Long, pagesSize: Int): Listing<Item> {
//        val sourceFactory = object : DataSourceFactory<Long, Item, ItemItemKeyedDataSource>() {
//            override fun createSource(): ItemItemKeyedDataSource =
//                    ItemItemKeyedDataSource(apiService, schedulerProvider, itemId)
//
//        }
//        val pagedConfig = PagedList.Config.Builder()
//                .setEnablePlaceholders(true)
//                .setInitialLoadSizeHint(pagesSize)
//                .setPageSize(pagesSize)
//                .build()
//
//        val livePagedList = LivePagedListBuilder<Long, Item>(sourceFactory, pagedConfig)
//                .build()
//
//        return Listing(pagedList = livePagedList,
//                networkState = switchMap(sourceFactory.liveSource) { it.networkState }
//        )
//    }

    companion object {
        const val DEFAULT_NETWORK_PAGE_SIZE = 20
        const val DEFAULT_LOCAL_PAGE_SIZE = 30
    }
}