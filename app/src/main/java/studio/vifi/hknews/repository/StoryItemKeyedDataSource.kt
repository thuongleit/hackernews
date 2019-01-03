package studio.vifi.hknews.repository

import io.reactivex.Single
import studio.vifi.hknews.SchedulerProvider
import studio.vifi.hknews.api.ApiService
import studio.vifi.hknews.vo.StoryType
import studio.vifi.hknews.api.path

class StoryItemKeyedDataSource(
        api: ApiService,
        schedulerProvider: SchedulerProvider,
        private val storyType: StoryType)
    : ItemKeyedDataSource(api, schedulerProvider) {

    private var itemIds: List<Long> = listOf()

    override fun loadKidIds(): Single<List<Long>> {
        return if (this.itemIds.isEmpty()) {
            api
                    .getStories(storyType.path())
                    .doOnSuccess { this.itemIds = it }
        } else {
            Single.just(this.itemIds)
        }
    }
}