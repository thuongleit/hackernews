package me.thuongle.hknews.view.story

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import me.thuongle.hknews.R
import me.thuongle.hknews.util.DialogFactory
import me.thuongle.hknews.view.base.BaseFragment
import me.thuongle.hknews.view.base.BasePresenter
import javax.inject.Inject

class StoryContentFragment : BaseFragment(), StoryContract.View {
    @Inject internal
    lateinit var presenter: StoryContract.Presenter

    override fun getPresenter(): BasePresenter? = presenter

    private lateinit var layoutContainer: FrameLayout
    private lateinit var viewProgressBar: ProgressBar
    private var layoutNoInternetError: View? = null
    private lateinit var contentUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentUrl = arguments?.getString(ARG_CONTENT_URL) ?: ""

        DaggerStoryComponent
                .builder()
                .applicationComponent(component)
                .storyModule(StoryModule(this, contentUrl))
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_story_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutContainer = view.findViewById(R.id.container) as FrameLayout
        viewProgressBar = view.findViewById(R.id.progressbar) as ProgressBar
        val webViewContent = view.findViewById(R.id.webview_content) as WebView
//        webViewContent.setWebViewClient(webClient)
        webViewContent.loadUrl(contentUrl)
    }

    override fun showNetworkError(t: Throwable) {
        Log.d(TAG, "No Internet. ${t.message}")
        if (layoutNoInternetError == null) {
            layoutNoInternetError = LayoutInflater.from(context).inflate(R.layout.view_network_error, null)
            (layoutNoInternetError!!.findViewById(R.id.btn_retry) as View).setOnClickListener {
                presenter.subscribe()
            }
        }
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        layoutContainer.addView(layoutNoInternetError, layoutParams)
    }

    override fun showInAppError(t: Throwable) {
        DialogFactory.createGenericErrorDialog(context!!).show()
    }

    companion object {
        private val TAG = "StoryContentFragment"
        private val ARG_CONTENT_URL = "StoryContentFragment.ARG_CONTENT_URL"

        fun newInstance(contentUrl: String): Fragment {
            val fragment = StoryContentFragment()
            val args = Bundle()
            args.putString(ARG_CONTENT_URL, contentUrl)
            fragment.arguments = args

            return fragment
        }
    }
}