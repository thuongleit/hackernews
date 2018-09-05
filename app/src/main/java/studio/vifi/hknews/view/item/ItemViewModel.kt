package studio.vifi.hknews.view.item

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import studio.vifi.hknews.data.model.StoryType
import studio.vifi.hknews.data.repository.ItemRepository
import javax.inject.Inject

class ItemViewModel @Inject constructor(itemRepository: ItemRepository) : ViewModel() {

    private val liveRequestType = MutableLiveData<StoryType>()

    private val liveResult = map(liveRequestType, { itemRepository.loadItems(it) })

    val liveItems = switchMap(liveResult, { it.data })

    val liveNetworkState = switchMap(liveResult, { it.networkState })

    val liveRefreshState = switchMap(liveResult, { it.refreshState })

    fun refresh() {
        liveResult.value?.refresh?.invoke()
    }

    fun retry() {
        liveResult.value?.retry?.invoke()
    }

    fun requestItems(type: StoryType): Boolean {
        if (liveRequestType.value == type) {
            return false
        }
        liveRequestType.value = type
        return true
    }
}