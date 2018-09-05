package studio.vifi.hknews.view.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import studio.vifi.hknews.data.model.Item
import studio.vifi.hknews.data.repository.ItemRepository
import studio.vifi.hknews.data.repository.NetworkState
import javax.inject.Inject

class ItemCommentViewModel @Inject constructor(itemRepository: ItemRepository) : ViewModel() {

    private val itemId: MutableLiveData<Long> = MutableLiveData()

    private val liveData = map(itemId) {
        itemRepository.fetchDirectKids(it, 20)
    }

    val comments: LiveData<PagedList<Item>> = switchMap(liveData) { data ->
        data.pagedList
    }

    val networkState: LiveData<NetworkState> = switchMap(liveData) {
        it.networkState
    }

    fun setId(itemId: Long): Boolean {
        if (this.itemId.value == itemId) {
            return false
        }
        this.itemId.value = itemId
        return true
    }
}