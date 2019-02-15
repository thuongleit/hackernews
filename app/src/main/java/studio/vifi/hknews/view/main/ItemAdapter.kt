package studio.vifi.hknews.view.main

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import studio.vifi.hknews.R
import studio.vifi.hknews.databinding.StoryItemBinding
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.util.customtabs.CustomTabActivityHelper
import studio.vifi.hknews.view.common.DataBoundListAdapter
import studio.vifi.hknews.view.common.createBinding

class ItemAdapter(private val activity: Activity) : DataBoundListAdapter<Item, StoryItemBinding>(
        DIFF_CALLBACK
) {
    val onItemClick: (View, Item) -> Unit = { view, item ->
        CustomTabActivityHelper.openCustomTab(
                activity,
                CustomTabsIntent.Builder()
                        .setToolbarColor(
                                ContextCompat.getColor(
                                        activity,
                                        R.color.colorPrimary
                                )
                        )
                        .setShowTitle(true)
                        .enableUrlBarHiding()
                        .setSecondaryToolbarColor(ContextCompat.getColor(
                                activity,
                                android.R.color.white
                        ))
                        .addDefaultShareMenuItem()
                        .build(),
                item.url?.toUri()
        )
    }

    override fun createItemBinding(parent: ViewGroup): StoryItemBinding {
        return createBinding(parent, R.layout.story_item) as StoryItemBinding
    }

    override fun bindItem(binding: StoryItemBinding, item: Item) {
        binding.item = item
        binding.adapter = this
    }

    companion object {
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