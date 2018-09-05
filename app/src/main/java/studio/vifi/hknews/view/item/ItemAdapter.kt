package studio.vifi.hknews.view.item

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.vifi.hknews.R
import studio.vifi.hknews.data.model.Item
import studio.vifi.hknews.data.repository.ERROR
import studio.vifi.hknews.data.repository.LOADING
import studio.vifi.hknews.databinding.NetworkStateItemBinding
import studio.vifi.hknews.databinding.StoryItemBinding
import studio.vifi.hknews.view.common.NetworkDataBoundListAdapter
import studio.vifi.hknews.view.detail.StoryActivity
import studio.vifi.hknews.view.detail.StoryActivity.Companion.SHARED_VIEW_TOOLBAR_TITLE

class ItemAdapter(private val activity: Activity) : NetworkDataBoundListAdapter<Item, StoryItemBinding, NetworkStateItemBinding>(
        DIFF_CALLBACK
) {
    val onItemClick: (View, Item) -> Unit = { view, item ->
        val intent = StoryActivity.newInstance(activity, item)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val sceneTransitionAnimation = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    view.findViewById(R.id.tv_title),
                    SHARED_VIEW_TOOLBAR_TITLE
            )

            ActivityCompat.startActivity(
                    activity,
                    intent,
                    sceneTransitionAnimation.toBundle())
        } else {
            activity.startActivity(intent)
        }
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
            is ERROR -> {
                binding.failed = true
                binding.message = (networkState as ERROR).error?.message ?: "unknown error"
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