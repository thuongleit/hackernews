package studio.vifi.hknews.binding

import android.databinding.BindingAdapter
import android.text.Html
import android.view.View
import android.webkit.WebView
import android.widget.TextView

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibility")
    fun setVisibility(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("fromHtml")
    fun fromHtml(tv: TextView, text: String?) {
        tv.text = if (text.isNullOrEmpty()) {
            ""
        } else {
            Html.fromHtml(text?.trim())
        }
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
    @BindingAdapter("url")
    fun setUrl(wv: WebView, url: String) {
        wv.loadUrl(url)
    }
}
