package studio.vifi.hknews.model.repository

import studio.vifi.hknews.LiveResult
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.model.vo.StoryType

interface ItemRepository {
    fun fetchStories(type: StoryType): LiveResult<List<Long>>
    fun fetchItem(itemId: Long): LiveResult<Item>
}