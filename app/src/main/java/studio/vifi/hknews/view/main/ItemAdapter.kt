package studio.vifi.hknews.view.main

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import studio.vifi.hknews.R
import studio.vifi.hknews.databinding.StoryItemBinding
import studio.vifi.hknews.model.api.BROWSER_ID_URL
import studio.vifi.hknews.model.api.BROWSER_VOTE_ID_URL
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.openUrlInCustomTab
import studio.vifi.hknews.view.common.DataBoundListAdapter
import studio.vifi.hknews.view.common.createBinding

class ItemAdapter(private val activity: Activity) : DataBoundListAdapter<Item, StoryItemBinding>(
        DIFF_CALLBACK
) {
    private var isLoading = false

    val onItemClick: (View, Item) -> Unit = { _, item ->
        if (!item.url.isNullOrBlank()) {
            openUrlInCustomTab(activity, item.url)
        } else {
            openUrlInCustomTab(activity, String.format(BROWSER_ID_URL, item.id))
        }
    }

    val onItemCommentClick: (Item) -> Unit = { item: Item ->
        openUrlInCustomTab(activity, String.format(BROWSER_ID_URL, item.id))
    }

    val onItemVoteClick: (Item) -> Unit = { item: Item ->
        openUrlInCustomTab(activity, String.format(BROWSER_VOTE_ID_URL, item.id))
    }

    override fun createItemBinding(parent: ViewGroup): StoryItemBinding {
        return createBinding(parent, R.layout.story_item) as StoryItemBinding
    }

    override fun bindItem(binding: StoryItemBinding, item: Item) {
        binding.item = item
        binding.adapter = this
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun isLoading(): Boolean {
        return isLoading
    }

    companion object {
        const val VISIBLE_THRESHOLD = 3

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.title == newItem.title
                        && oldItem.time == newItem.time
            }
        }
    }
}