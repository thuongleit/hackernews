package studio.vifi.hknews.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.vifi.hknews.LiveResult
import studio.vifi.hknews.MediatorLiveResult
import studio.vifi.hknews.model.usecase.FetchStoriesUseCase
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.model.vo.StoryType
import javax.inject.Inject

class ItemViewModel @Inject constructor(private val useCase: FetchStoriesUseCase) : ViewModel() {
    val result: LiveResult<List<Item>> = MediatorLiveResult()

    private val requestedStoryType: MutableLiveData<StoryType> = MutableLiveData()

    init {
        val dataSource = useCase.observe()
        (result as MediatorLiveResult<List<Item>>).addSource(dataSource) { data ->
            result.postValue(data)
        }

        result.addSource(requestedStoryType) { newRequest ->
            newRequest?.let { useCase.execute(it) }
        }
    }

    fun fetchItems(type: StoryType): Boolean {
        if (requestedStoryType.value == type) {
            return false
        }
        requestedStoryType.value = type
        return true
    }
}