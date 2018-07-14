package me.thuongle.hknews.view.main

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.thuongle.hknews.R
import me.thuongle.hknews.api.Item
import me.thuongle.hknews.databinding.NetworkStateItemBinding
import me.thuongle.hknews.databinding.StoryItemBinding
import me.thuongle.hknews.repository.FAILED
import me.thuongle.hknews.repository.LOADING
import me.thuongle.hknews.view.common.NetworkDataBoundListAdapter
import me.thuongle.hknews.view.story.StoryActivity

class ItemAdapter : NetworkDataBoundListAdapter<Item, StoryItemBinding, NetworkStateItemBinding>(
        DIFF_CALLBACK
) {
    val onItemClick: (View, Item) -> Unit = { view, item ->
        view.context.startActivity(StoryActivity.newInstance(view.context, item))
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
            is LOADING -> binding.loading = true
            is FAILED -> {
                binding.failed = true
                binding.message = (networkState as FAILED).msg
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