package studio.vifi.hknews.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.vifi.hknews.*
import studio.vifi.hknews.model.usecase.FetchStoriesUseCase
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.model.vo.StoryType
import javax.inject.Inject

class ItemViewModel @Inject constructor(private val useCase: FetchStoriesUseCase) : ViewModel() {

    val result: LiveResult<List<Item>> = MediatorLiveResult()
    val nextPageLoadingStatus: MutableLiveData<Result<Unit>> = MutableLiveData()

    private val requestedStoryType: MutableLiveData<StoryType> = MutableLiveData()

    init {
        val dataSource = useCase.observe()
        (result as MediatorLiveResult<List<Item>>).addSource(dataSource) { data ->
            if (nextPageLoadingStatus.value is Result.Running) {
                nextPageLoadingStatus.postValueIfNew(data.map { Unit })
            }
            result.postValue(data)
        }

        result.addSource(requestedStoryType) { newRequest ->
            newRequest?.let { useCase.execute(it) }
        }
    }

    fun fetchItems(type: StoryType, force: Boolean = false): Boolean {
        if (force) {
            requestedStoryType.value = type
            return true
        }

        if (requestedStoryType.value == type) {
            return false
        }
        requestedStoryType.value = type
        return true
    }


    fun loadMore(): Boolean {
        nextPageLoadingStatus.postValueIfNew(Result.Running(Unit))
        val loadNextPageRunning = useCase.loadNextPage()
        if (!loadNextPageRunning) {
            nextPageLoadingStatus.postValue(Result.Success(Unit))
        }

        return loadNextPageRunning
    }
}