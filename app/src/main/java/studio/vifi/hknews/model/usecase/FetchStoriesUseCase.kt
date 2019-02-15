package studio.vifi.hknews.model.usecase

import studio.vifi.hknews.Result
import studio.vifi.hknews.map
import studio.vifi.hknews.model.repository.ItemRepository
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.model.vo.StoryType
import studio.vifi.hknews.postValueIfNew
import javax.inject.Inject

class FetchStoriesUseCase @Inject constructor(private val repository: ItemRepository)
    : MediatorUseCase<StoryType, List<Item>>() {

    private val stories = mutableListOf<Long>()
    private val data = mutableSetOf<Item>()
    private var requestedPos: Int = 0

    override fun execute(parameters: StoryType) {
        reset()
        val executeSource = repository.fetchStories(parameters)

        result.removeSource(executeSource)
        result.addSource(executeSource) { resultData ->
            when (resultData) {
                is Result.Success -> {
                    resultData.data?.let { stories.addAll(it) }
                    request(stories, 0)
                }
                else -> {
                    result.postValueIfNew(resultData.map { emptyList<Item>() })
                }
            }
        }
    }

    private fun reset() {
        data.clear()
    }

    fun loadNextPage(): Boolean {
        if (stories.isEmpty()) {
            return false
        }
        if (requestedPos > stories.size) {
            return false
        }

        request(stories, requestedPos + 1)
        return true
    }

    private fun request(stories: List<Long>, atIndex: Int) {
        stories.forEachIndexed { index, id ->
            if (index >= atIndex && index <= atIndex + PAGE_SIZE) {
                requestedPos = index
                val itemSource = repository.fetchItem(id)

                result.removeSource(itemSource)
                result.addSource(itemSource) { itemResult: Result<Item>? ->
                    if (itemResult is Result.Success) {
                        if (itemResult.data != null) {
                            data.add(itemResult.data)
                            result.postValue(Result.Success(data.toList()))
                        }
                    }
                }

                if (index > atIndex + PAGE_SIZE) {
                    return@forEachIndexed
                }
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}