package me.thuongle.hknews.repository

import io.reactivex.Single
import me.thuongle.hknews.SchedulerProvider
import me.thuongle.hknews.api.ApiService
import me.thuongle.hknews.vo.StoryType
import me.thuongle.hknews.api.path

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