package studio.vifi.hknews.data.repository

import android.arch.lifecycle.Transformations.switchMap
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import studio.vifi.hknews.AppExecutors
import studio.vifi.hknews.data.api.ApiService
import studio.vifi.hknews.data.model.Item
import studio.vifi.hknews.data.model.StoryType
import javax.inject.Inject

class ItemRepository @Inject constructor(
        private val apiService: ApiService,
        private val appExecutors: AppExecutors
) {
    fun fetchItems(type: StoryType,
                   pagesSize: Int = DEFAULT_REQUEST_PAGE_SIZE): Listing<Item> {

        val sourceFactory = object : DataSourceFactory<Long, Item, ItemKeyedDataSource>() {
            override fun createSource(): ItemKeyedDataSource =
                    ItemKeyedDataSource(apiService, type, appExecutors.networkIO())
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

        val sourceFactory = object : DataSourceFactory<Long, Item, ItemKeyedDataSource>() {
            override fun createSource(): ItemKeyedDataSource =
                    ItemKeyedDataSource(apiService, StoryType.ASK, appExecutors.networkIO())
        }
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(pagesSize)
                .setPageSize(pagesSize)
                .build()

        val livePagedList = LivePagedListBuilder<Long, Item>(sourceFactory, config)
                .build()

        return Listing(pagedList = livePagedList,
                networkState = switchMap(sourceFactory.liveSource) { it.networkState })

    }

    companion object {
        const val DEFAULT_REQUEST_PAGE_SIZE = 20
    }
}