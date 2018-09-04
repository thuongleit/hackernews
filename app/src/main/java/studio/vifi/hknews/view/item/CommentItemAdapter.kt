package studio.vifi.hknews.view.item

import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import studio.vifi.hknews.R
import studio.vifi.hknews.data.vo.Item
import studio.vifi.hknews.databinding.CommentChildItemBinding
import studio.vifi.hknews.databinding.CommentParentItemBinding
import studio.vifi.hknews.view.common.ExpendableDataBoundListAdapter
import studio.vifi.hknews.view.common.createBinding

class CommentItemAdapter : ExpendableDataBoundListAdapter<Item, CommentParentItemBinding, CommentChildItemBinding>(
        DIFF_CALLBACK
) {
    override fun createParentBinding(parent: ViewGroup): CommentParentItemBinding {
        return createBinding(parent, R.layout.comment_parent_item) as CommentParentItemBinding
    }

    override fun createChildBinding(parent: ViewGroup): CommentChildItemBinding {
        return createBinding(parent, R.layout.comment_child_item) as CommentChildItemBinding
    }

    override fun bindParentItem(binding: CommentParentItemBinding, item: Item) {
        binding.item = item
    }

    override fun bindChildItem(binding: CommentChildItemBinding, item: Item) {
    }

    override fun getChildCount(): Int = 1

    override fun isParent(position: Int): Boolean = true

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