package studio.vifi.hknews.view.item

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import studio.vifi.hknews.data.model.Item
import studio.vifi.hknews.data.model.StoryType
import studio.vifi.hknews.data.repository.ItemRepository
import studio.vifi.hknews.data.repository.NetworkState
import javax.inject.Inject

class ItemViewModel @Inject constructor(itemRepository: ItemRepository) : ViewModel() {

    private val storyType: MutableLiveData<StoryType> = MutableLiveData()
    private val liveData = map(storyType) {
        itemRepository.fetchItems(it, 20)
    }

    val stories: LiveData<PagedList<Item>> = switchMap(liveData) { data ->
        data.pagedList
    }

    val networkState: LiveData<NetworkState> = switchMap(liveData) {
        it.networkState
    }

    fun showStory(type: StoryType): Boolean {
        if (storyType.value == type) {
            return false
        }
        storyType.value = type
        return true
    }
}