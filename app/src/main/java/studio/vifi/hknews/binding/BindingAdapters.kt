package studio.vifi.hknews.binding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import studio.vifi.hknews.R
import studio.vifi.hknews.view.common.Callback

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibility")
    fun setVisibility(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("text", "textEmptyIfZero")
    fun setText(tv: TextView, anInt: Int, show: Boolean = true) {
        val text = if (!show && anInt == 0) {
            ""
        } else {
            anInt.toString()
        }
        tv.text = text
    }

    @JvmStatic
    @BindingAdapter("text")
    fun setText(tv: TextView, anInt: Int) {
        tv.text = anInt.toString()
    }

    @JvmStatic
    @BindingAdapter("error")
    fun connectionOnOffBasedOnErrorMsg(tv: TextView, text: String?) {
        if (tv.context.getString(R.string.error_connection_lost_message) == text) {
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_no_internet, 0, 0)
        } else {
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }
        tv.text = text
    }

    @JvmStatic
    @BindingAdapter("callback")
    fun setOnClickCallback(v: View, callback: Callback?) {
        v.setOnClickListener { callback?.invoke(v) }
    }
}
