package studio.vifi.hknews.view.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

abstract class NetworkDataBoundListAdapter<T, V : ViewDataBinding, N : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
) : DataBoundListAdapter<T, V>(diffCallback) {

    protected var networkState: studio.vifi.hknews.Result<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<*> {
        return when (viewType) {
            DATA_TYPE -> super.onCreateViewHolder(parent, viewType)
            NETWORK_TYPE -> DataBoundViewHolder(createNetworkBinding(parent))
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<*>, position: Int) {
        when (getItemViewType(position)) {
            DATA_TYPE -> super.onBindViewHolder(holder, position)
            NETWORK_TYPE -> {
                bindNetworkState(holder.binding as N)
                holder.binding.executePendingBindings()
            }
        }
    }

    private fun hasExtraRow() = (networkState != null && networkState !is studio.vifi.hknews.Result.Running)

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_TYPE
        } else {
            DATA_TYPE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setState(newNetworkState: studio.vifi.hknews.Result<T>?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }


    protected abstract fun createNetworkBinding(parent: ViewGroup): N
    protected abstract fun bindNetworkState(binding: N)

    companion object {
        const val DATA_TYPE = 0
        const val NETWORK_TYPE = 1
    }
}
