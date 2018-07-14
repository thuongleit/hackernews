package me.thuongle.hknews.view.common

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <V> The type of the ViewDataBinding.
</V> */
class DataBoundViewHolder<out V : ViewDataBinding> constructor(val binding: V) :
        RecyclerView.ViewHolder(binding.root)