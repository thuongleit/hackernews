package studio.vifi.hknews.view.main

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import studio.vifi.hknews.R
import studio.vifi.hknews.databinding.NetworkStateItemBinding
import studio.vifi.hknews.databinding.StoryItemBinding
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.util.customtabs.CustomTabActivityHelper
import studio.vifi.hknews.view.common.NetworkDataBoundListAdapter

class ItemAdapter(private val activity: Activity) : NetworkDataBoundListAdapter<Item, StoryItemBinding, NetworkStateItemBinding>(
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

    override fun createNetworkBinding(parent: ViewGroup): NetworkStateItemBinding {
        return createBinding(parent, R.layout.network_state_item) as NetworkStateItemBinding
    }

    override fun bindItem(binding: StoryItemBinding, item: Item) {
        binding.item = item
        binding.adapter = this
    }

    override fun bindNetworkState(binding: NetworkStateItemBinding) {
        when (networkState) {
            is studio.vifi.hknews.Result.Running -> binding.loading = true
            is studio.vifi.hknews.Result.Failure -> {
                binding.failed = true
                binding.message = (networkState as studio.vifi.hknews.Result.Failure).exception.message
            }
        }
    }

    private fun createBinding(parent: ViewGroup, layoutId: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
        )
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