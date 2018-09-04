package studio.vifi.hknews.view.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import studio.vifi.hknews.data.repository.ItemRepository
import studio.vifi.hknews.data.vo.Item
import javax.inject.Inject

class StoryViewModel @Inject constructor(itemRepository: ItemRepository) : ViewModel() {

    private val storyType: MutableLiveData<Item.StoryType> = MutableLiveData()

    val stories: LiveData<PagedList<Item>> = switchMap(storyType) {
        itemRepository.fetchStories(type = it)
    }

    fun loadStories(type: Item.StoryType): Boolean {
        if (storyType.value == type) {
            return false
        }
        storyType.value = type
        return true
    }
}