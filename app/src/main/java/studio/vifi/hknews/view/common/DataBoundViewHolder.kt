package studio.vifi.hknews.view.common

import androidx.databinding.ViewDataBinding

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <V> The type of the ViewDataBinding.
</V> */
class DataBoundViewHolder<out V : ViewDataBinding> constructor(val binding: V) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)