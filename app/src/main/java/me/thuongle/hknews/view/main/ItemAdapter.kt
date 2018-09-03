package me.thuongle.hknews.view.main

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
import me.thuongle.hknews.R
import me.thuongle.hknews.data.vo.Item
import me.thuongle.hknews.databinding.StoryItemBinding
import me.thuongle.hknews.view.common.DataBoundListAdapter
import me.thuongle.hknews.view.story.StoryActivity
import me.thuongle.hknews.view.story.StoryActivity.Companion.SHARED_VIEW_TOOLBAR_TITLE

class ItemAdapter(private val activity: Activity) : DataBoundListAdapter<Item, StoryItemBinding>(
        DIFF_CALLBACK
) {
    val onItemClick: (View, Item) -> Unit = { view, item ->
        val intent = StoryActivity.newInstance(activity, item.id)
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

    override fun bindItem(binding: StoryItemBinding, item: Item) {
        binding.item = item
        binding.adapter = this
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