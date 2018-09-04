package studio.vifi.hknews.view.story

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import studio.vifi.hknews.data.api.NetworkState
import studio.vifi.hknews.data.repository.ItemRepository
import studio.vifi.hknews.data.vo.Item
import javax.inject.Inject

class StoryViewModel @Inject constructor(itemRepository: ItemRepository) : ViewModel() {

    private val liveStoryType: MutableLiveData<Item.StoryType> = MutableLiveData()

    val liveRequest = map(liveStoryType) {
        itemRepository.fetchStories(it)
    }
    val liveStories: LiveData<PagedList<Item>> = switchMap(liveRequest) {
        it.pagedList
    }
    val liveNetworkState: LiveData<NetworkState> = switchMap(liveRequest) {
        it.networkState
    }

    fun loadStories(type: Item.StoryType): Boolean {
        if (liveStoryType.value == type) {
            return false
        }
        liveStoryType.value = type
        return true
    }
}