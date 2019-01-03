package studio.vifi.hknews.repository

import io.reactivex.Single
import studio.vifi.hknews.SchedulerProvider
import studio.vifi.hknews.api.ApiService

class ItemItemKeyedDataSource(
        api: ApiService,
        schedulerProvider: SchedulerProvider,
        private val itemId: Long)
    : ItemKeyedDataSource(api, schedulerProvider) {

    private var itemIds: List<Long> = listOf()

    override fun loadKidIds(): Single<List<Long>> {
        return if (itemIds.isEmpty()) {
            api.getItemDetail(itemId)
                    .map { it.kids }
                    .map {
                        itemIds = it
                        itemIds
                    }
        } else {
            Single.just(itemIds)
        }
    }
} 