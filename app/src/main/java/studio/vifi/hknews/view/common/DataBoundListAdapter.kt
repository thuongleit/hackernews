package studio.vifi.hknews.view.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <T> Type of the items in the list
 * @param <B> The type of the ViewDataBinding
</V></T> */
abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundViewHolder<*>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<*> =
            DataBoundViewHolder(createItemBinding(parent))

    @Suppress("UNCHECKED_CAST")
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