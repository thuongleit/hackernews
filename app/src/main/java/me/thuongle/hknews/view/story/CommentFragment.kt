package me.thuongle.hknews.view.story

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import me.thuongle.hknews.R
import me.thuongle.hknews.util.DialogFactory
import me.thuongle.hknews.view.base.BaseFragment
import me.thuongle.hknews.view.base.BasePresenter
import javax.inject.Inject

class CommentFragment : BaseFragment(), CommentsContract.View {
    @Inject internal
    lateinit var presenter: CommentsContract.Presenter

    override fun getPresenter(): BasePresenter? = presenter

    private lateinit var layoutContainer: FrameLayout
    private lateinit var viewProgressBar: ProgressBar
    private var layoutNoInternetError: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storyId = arguments?.getLong(ARG_STORY_ID) ?: 0L

        DaggerCommentComponent
                .builder()
                .applicationComponent(component)
                .commentModule(CommentModule(this, storyId))
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutContainer = view.findViewById(R.id.container) as FrameLayout
        viewProgressBar = view.findViewById(R.id.progressbar) as ProgressBar
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
        private val ARG_STORY_ID = "StoryContentFragment.ARG_STORY_ID"

        fun newInstance(storyId: Long): Fragment {
            val fragment = CommentFragment()
            val args = Bundle()
            args.putLong(ARG_STORY_ID, storyId)
            fragment.arguments = args

            return fragment
        }
    }
}