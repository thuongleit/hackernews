package studio.vifi.hknews.view.common

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <T> Type of the items in the list
 * @param <B> The type of the ViewDataBinding
</V></T> */
abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, DataBoundViewHolder<*>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<*> =
            DataBoundViewHolder(createItemBinding(parent))

    override fun onBindViewHolder(holder: DataBoundViewHolder<*>, position: Int) {
        getItem(position)?.let { bindItem(holder.binding as V, it) }
        holder.binding.executePendingBindings()
    }

    protected abstract fun createItemBinding(parent: ViewGroup): V
    protected abstract fun bindItem(binding: V, item: T)
}

fun createBinding(parent: ViewGroup, layoutId: Int): ViewDataBinding {
    return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
    )
}