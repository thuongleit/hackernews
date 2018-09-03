package me.thuongle.hknews.data.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import androidx.work.*
import me.thuongle.hknews.SchedulerProvider
import me.thuongle.hknews.data.api.ApiService
import me.thuongle.hknews.data.db.HackerNewsDb
import me.thuongle.hknews.data.vo.Item
import me.thuongle.hknews.data.worker.FetchStoriesWorker
import me.thuongle.hknews.data.worker.FetchStoriesWorker.Companion.KEY_REQUEST_STORY_TYPE_ORDINAL
import javax.inject.Inject

class ItemRepository @Inject constructor(
        private val apiService: ApiService,
        private val database: HackerNewsDb,
        private val schedulerProvider: SchedulerProvider
) {
    fun fetchStories(type: Item.StoryType, requestPageSize: Int = DEFAULT_LOCAL_PAGE_SIZE): LiveData<PagedList<Item>> {
        val instance = WorkManager.getInstance()
        instance.let { workManager ->
            //schedule a worker to do the rest
            val data = mapOf(
                    KEY_REQUEST_STORY_TYPE_ORDINAL to type.ordinal
            ).toWorkData()
            val request = OneTimeWorkRequest.Builder(FetchStoriesWorker::class.java)
                    .setInputData(data)
                    .setConstraints(
                            Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build())
                    .build()
            workManager.enqueue(request)
        }

        val itemDataSource = database.itemDao().loadByType(type)

        val pagedConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(requestPageSize)
                .setPageSize(requestPageSize * 2)
                .build()

        return LivePagedListBuilder<Int, Item>(itemDataSource, pagedConfig).build()
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