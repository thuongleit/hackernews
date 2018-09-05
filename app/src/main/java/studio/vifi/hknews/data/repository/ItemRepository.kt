package studio.vifi.hknews.data.repository

import android.arch.lifecycle.Transformations.switchMap
import android.arch.paging.LivePagedListBuilder
import studio.vifi.hknews.AppExecutors
import studio.vifi.hknews.data.api.ApiService
import studio.vifi.hknews.data.model.Item
import studio.vifi.hknews.data.model.StoryType
import javax.inject.Inject

class ItemRepository @Inject constructor(
        private val apiService: ApiService,
        private val appExecutors: AppExecutors
) {
    fun loadItems(type: StoryType,
                  requestSize: Int = DEFAULT_REQUEST_PAGE_SIZE): Listing<Item> {

        val sourceFactory = object : DataSourceFactory<Long, Item, ItemKeyedDataSource>() {
            override fun createSource(): ItemKeyedDataSource =
                    ItemKeyedDataSource(apiService, type, appExecutors.networkIO())
        }
        val livePagedList = LivePagedListBuilder<Long, Item>(sourceFactory, requestSize)
                .setFetchExecutor(appExecutors.networkIO())
                .build()

        return Listing(data = livePagedList,
                networkState = switchMap(sourceFactory.liveDataSource, {it.networkState}),
                refresh = {
                    sourceFactory.liveDataSource.value?.refresh()
                },
                retry = {
                    sourceFactory.liveDataSource.value?.retry()
                })
    }

    fun fetchDirectKids(itemId: Long,
                        requestSize: Int = DEFAULT_REQUEST_PAGE_SIZE): Listing<Item> {

        val sourceFactory = object : DataSourceFactory<Long, Item, ItemKeyedDataSource>() {
            override fun createSource(): ItemKeyedDataSource =
                    ItemKeyedDataSource(apiService, StoryType.BEST, appExecutors.networkIO())
        }
        val livePagedList = LivePagedListBuilder<Long, Item>(sourceFactory, requestSize)
                .setFetchExecutor(appExecutors.networkIO())
                .build()

        return Listing(data = livePagedList,
                networkState = switchMap(sourceFactory.liveDataSource, {it.networkState}),
                refresh = {
                    sourceFactory.liveDataSource.value?.refresh()
                },
                retry = {
                    sourceFactory.liveDataSource.value?.retry()
                })

    }

    companion object {
        const val DEFAULT_REQUEST_PAGE_SIZE = 10
    }
}