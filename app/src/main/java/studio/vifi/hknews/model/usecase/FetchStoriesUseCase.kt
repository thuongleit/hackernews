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

    private val data = mutableSetOf<Item>()

    override fun execute(parameters: StoryType) {
        val executeSource = repository.fetchStories(parameters)

        result.removeSource(executeSource)
        result.addSource(executeSource) { resultData ->
            if (resultData is Result.Success) {
                resultData.data?.forEach {
                    val itemSource = repository.fetchItem(it)
                    result.removeSource(itemSource)
                    result.addSource(itemSource) { itemResult: Result<Item>? ->
                        if (itemResult is Result.Success) {
                            if (itemResult.data != null) {
                                data.add(itemResult.data)
                                result.postValueIfNew(Result.Success(data.toList()))
                            }
                        }
                    }
                }
            } else {
                result.postValue(resultData.map { emptyList<Item>() })
            }
        }
    }
}
