package studio.vifi.hknews.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.vifi.hknews.LiveResult
import studio.vifi.hknews.MediatorLiveResult
import studio.vifi.hknews.Result
import studio.vifi.hknews.map
import studio.vifi.hknews.model.usecase.FetchStoriesUseCase
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.model.vo.StoryType
import javax.inject.Inject

class ItemViewModel @Inject constructor(private val useCase: FetchStoriesUseCase) : ViewModel() {

    val result: LiveResult<List<Item>> = MediatorLiveResult()
    val nextPageLoadingStatus = MutableLiveData<Result<Unit>>()
    val refreshStatus = MutableLiveData<Result<Unit>>()

    private val requestedStoryType: MutableLiveData<StoryType> = MutableLiveData()

    init {
        val dataSource = useCase.observe()
        (result as MediatorLiveResult<List<Item>>).addSource(dataSource) { resultData ->
            if (nextPageLoadingStatus.value is Result.Running) {
                nextPageLoadingStatus.postValue(resultData.map { Unit })
            }

            if (refreshStatus.value is Result.Running) {
                if (resultData !is Result.Success) {
                    return@addSource
                } else {
                    refreshStatus.postValue(Result.Success(Unit))
                }
            }
            result.postValue(resultData)
        }

        result.addSource(requestedStoryType) { newRequest ->
            newRequest?.let { useCase.execute(it) }
        }
    }

    fun refresh() {
        refreshStatus.postValue(Result.Running(Unit))
        useCase.refresh()
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

    fun canLoadMore() = useCase.canLoadMore()

    fun loadMore(): Boolean {
        nextPageLoadingStatus.postValue(Result.Running(Unit))
        val loadNextPageRunning = useCase.loadNextPage()
        if (!loadNextPageRunning) {
            nextPageLoadingStatus.postValue(Result.Success(Unit))
        }

        return loadNextPageRunning
    }

}