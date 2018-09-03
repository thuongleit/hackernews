package me.thuongle.hknews.data.worker

import androidx.work.Worker
import me.thuongle.hknews.data.api.ApiService
import me.thuongle.hknews.data.api.path
import me.thuongle.hknews.data.db.ItemDao
import me.thuongle.hknews.data.vo.Item
import me.thuongle.hknews.di.AndroidWorkerInjection
import timber.log.Timber
import javax.inject.Inject

class FetchStoriesWorker : Worker() {

    @Inject
    lateinit var itemDao: ItemDao

    @Inject
    lateinit var api: ApiService

    override fun doWork(): Result {
        AndroidWorkerInjection.inject(this)

        val requestStoryType = inputData.getInt(KEY_REQUEST_STORY_TYPE_ORDINAL, -1)

        if (requestStoryType == -1) {
            Timber.d("Wrong request story type")
            return Result.FAILURE
        }

        return try {
            val requestType = Item.StoryType.values()[requestStoryType]
            val storiesResponse = api
                    .getStories(requestType.path())
                    .execute()

            if (storiesResponse.isSuccessful) {
                storiesResponse.body()?.let { stories ->
                    stories.filterNot { id ->
                        itemDao.exists(id)
                    }.also { ids ->
                        var count = 0
                        val responseSize = ids.size
                        do {
                            val result = mutableListOf<Item>()
                            for (index in count until count + DEFAULT_PAGE_SIZE) {
                                val itemDetailResponse = api.getItemDetail(ids[index]).execute()

                                val item = if (itemDetailResponse.isSuccessful) {
                                    itemDetailResponse.body()?.copy(storyType = requestType)
                                } else {
                                    Timber.d(itemDetailResponse.message())
                                    null
                                }

                                if (item != null) {
                                    result.add(item)
                                }

                                if (count >= responseSize - 1) {
                                    break
                                } else {
                                    count++
                                }
                            }

                            itemDao.insert(*result.toTypedArray())
                            Timber.d("Insert to database ${result.size} items")
                            result.clear()
                        } while (count < responseSize - 1)
                    }
                }
                return Result.SUCCESS
            } else {
                Timber.e(storiesResponse.message())
                return Result.FAILURE
            }
        } catch (e: Exception) {
            Timber.e(e)
            Result.FAILURE
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val KEY_REQUEST_STORY_TYPE_ORDINAL = "KEY_REQUEST_STORY_TYPE_ORDINAL"
    }
}