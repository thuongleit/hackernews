package studio.vifi.hknews.view.common

import android.arch.paging.PagedListAdapter
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.ViewGroup

abstract class ExpendableDataBoundListAdapter<T, P : ViewDataBinding, C : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, DataBoundViewHolder<*>>(diffCallback) {

    val isExpanded: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<*> {
        return when (viewType) {
            PARENT_TYPE -> DataBoundViewHolder(createParentBinding(parent))
            CHILD_TYPE -> DataBoundViewHolder(createChildBinding(parent))
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<*>, position: Int) {
        when (getItemViewType(position)) {
            PARENT_TYPE -> getItem(position)?.let { bindParentItem(holder.binding as P, it) }
            CHILD_TYPE -> getItem(position)?.let { bindChildItem(holder.binding as C, it) }
        }
        holder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isParent(position)) PARENT_TYPE else CHILD_TYPE
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (isExpanded) getChildCount() else 0
    }

    protected abstract fun createParentBinding(parent: ViewGroup): P
    protected abstract fun createChildBinding(parent: ViewGroup): C
    protected abstract fun bindParentItem(binding: P, item: T)
    protected abstract fun bindChildItem(binding: C, item: T)
    protected abstract fun isParent(position: Int): Boolean
    protected abstract fun getChildCount(): Int

    companion object {
        const val PARENT_TYPE = 0
        const val CHILD_TYPE = 1
    }
}
