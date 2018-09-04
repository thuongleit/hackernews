package studio.vifi.hknews.view.story

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import studio.vifi.hknews.data.repository.ItemRepository
import studio.vifi.hknews.data.vo.Item
import javax.inject.Inject

class ItemCommentViewModel @Inject constructor(itemRepository: ItemRepository) : ViewModel() {

    private val itemId: MutableLiveData<Long> = MutableLiveData()

    val comments: LiveData<PagedList<Item>> = switchMap(itemId) { data ->
        null
    }

    fun setId(itemId: Long): Boolean {
        if (this.itemId.value == itemId) {
            return false
        }
        this.itemId.value = itemId
        return true
    }
}