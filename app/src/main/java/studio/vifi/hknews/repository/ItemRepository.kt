package studio.vifi.hknews.repository

import android.arch.lifecycle.Transformations.switchMap
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import studio.vifi.hknews.SchedulerProvider
import studio.vifi.hknews.api.ApiService
import studio.vifi.hknews.vo.Item
import studio.vifi.hknews.vo.StoryType
import javax.inject.Inject

class ItemRepository @Inject constructor(
        private val apiService: ApiService,
        private val schedulerProvider: SchedulerProvider
) {
    fun fetchStories(type: StoryType, pagesSize: Int): Listing<Item> {
        val sourceFactory = object : DataSourceFactory<Long, Item, StoryItemKeyedDataSource>() {
            override fun createSource(): StoryItemKeyedDataSource =
                    StoryItemKeyedDataSource(apiService, schedulerProvider, type)
        }
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(pagesSize)
                .setPageSize(pagesSize)
                .build()

        val livePagedList = LivePagedListBuilder<Long, Item>(sourceFactory, config)
                .build()

        return Listing(pagedList = livePagedList,
                networkState = switchMap(sourceFactory.liveSource) { it.networkState }
        )
    }

    fun fetchDirectKids(itemId: Long, pagesSize: Int): Listing<Item> {
        val sourceFactory = object : DataSourceFactory<Long, Item, ItemItemKeyedDataSource>() {
            override fun createSource(): ItemItemKeyedDataSource =
                    ItemItemKeyedDataSource(apiService, schedulerProvider, itemId)

        }
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(pagesSize)
                .setPageSize(pagesSize)
                .build()

        val livePagedList = LivePagedListBuilder<Long, Item>(sourceFactory, config)
                .build()

        return Listing(pagedList = livePagedList,
                networkState = switchMap(sourceFactory.liveSource) { it.networkState }
        )
    }
}