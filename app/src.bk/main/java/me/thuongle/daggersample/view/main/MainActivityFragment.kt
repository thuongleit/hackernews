package me.thuongle.daggersample.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
<<<<<<< HEAD
=======
import android.text.TextUtils
>>>>>>> 78784f95d605e34a7970daccd953330ebaed00c1
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
<<<<<<< HEAD
=======
import android.widget.ProgressBar
>>>>>>> 78784f95d605e34a7970daccd953330ebaed00c1
import android.widget.TextView
import android.widget.Toast
import me.thuongle.daggersample.R
import me.thuongle.daggersample.api.model.Item
import me.thuongle.daggersample.api.model.StoryType
import me.thuongle.daggersample.util.DialogFactory
import me.thuongle.daggersample.util.getBaseDomain
import me.thuongle.daggersample.util.getTimeAgo
import me.thuongle.daggersample.view.base.BaseFragment
import me.thuongle.daggersample.view.base.BasePresenter
import javax.inject.Inject


class MainActivityFragment : BaseFragment(), MainContract.View {
    @Inject internal
    lateinit var presenter: MainContract.Presenter

    override fun getPresenter(): BasePresenter? = presenter

    private lateinit var adapter: ItemRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutContainer: FrameLayout
    private lateinit var viewProgressBar: ProgressBar
    private var layoutNoInternetError: View? = null

    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisibleItemPosition = (recyclerView?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            val isLoading: Boolean = (presenter as MainPresenterImpl).isLoading
            if (!isLoading &&
                    lastVisibleItemPosition >= adapter.itemCount - MainPresenterImpl.LOADING_VISIBLE_THRESHOLD) {
                presenter.loadMore()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = arguments.getInt(ARG_NEWS_TYPE)

        DaggerMainComponent
                .builder()
                .applicationComponent(component)
                .mainModule(MainModule(this, type))
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

        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        adapter = ItemRecyclerViewAdapter(activity)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(onScrollListener)
    }

    override fun onDestroyView() {
        recyclerView.removeOnScrollListener(onScrollListener)
        super.onDestroyView()
    }

    override fun onReceiveData(item: Item) {
        adapter.add(item)
    }

    override fun onError(t: Throwable) {
        DialogFactory.createGenericErrorDialog(context).show()
    }

    override fun showNetworkError(t: Throwable) {
        Log.d(TAG, "No Internet. ${t.message}")
<<<<<<< HEAD
        showInAppError(t)
        val rootContent = activity.findViewById(android.R.id.content) as FrameLayout
        val layoutNoInternetError = LayoutInflater.from(context).inflate(R.layout.view_network_error, null)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        rootContent.addView(layoutNoInternetError, layoutParams)
    }

    override fun showInAppError(t: Throwable) {
        DialogFactory.createGenericErrorDialog(context).show()
=======
        if (layoutNoInternetError == null) {
            layoutNoInternetError = LayoutInflater.from(context).inflate(R.layout.view_network_error, null)
            layoutNoInternetError!!.findViewById(R.id.btn_retry).setOnClickListener {
                presenter.subscribe()
            }
        }
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        layoutContainer.addView(layoutNoInternetError, layoutParams)
    }

    override fun showInAppError(t: Throwable) {
        DialogFactory.createGenericErrorDialog(context).show()
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            viewProgressBar.visibility = View.VISIBLE
        } else {
            viewProgressBar.visibility = View.GONE
        }
    }

    override fun removeNetworkErrorLayoutIfNeeded() {
        if (layoutNoInternetError != null) {
            layoutContainer.removeView(layoutNoInternetError)
        }
>>>>>>> 78784f95d605e34a7970daccd953330ebaed00c1
    }

    companion object {
        private val TAG = "MainActivityFragment"
        private val ARG_NEWS_TYPE = "MainActivityFragment.ARG_NEWS_TYPE"

        fun newInstance(type: StoryType): MainActivityFragment {
            val fragment = MainActivityFragment()
            val args = Bundle()
            args.putInt(ARG_NEWS_TYPE, type.ordinal)
            fragment.arguments = args

            return fragment
        }
    }

    private class ItemRecyclerViewAdapter(val context: Context) : RecyclerView.Adapter<ItemRecyclerViewAdapter.StoryViewHolder>() {
        val data: MutableList<Item> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StoryViewHolder
                = StoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_story, parent, false))

        override fun onBindViewHolder(holderStory: StoryViewHolder, position: Int) {
            holderStory.bind(data[position])
        }

        override fun getItemCount(): Int = data.size

        fun add(item: Item) {
            if (!data.contains(item)) {
                data.add(item)
                notifyItemInserted(data.size - 1)
            }
        }

        inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            init {
                itemView.setOnClickListener {
                    val url = data[adapterPosition].url

                    if (url == null) {
                        url ?: Toast.makeText(context, "Url is empty", Toast.LENGTH_SHORT).show();
                        return@setOnClickListener
                    }

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }
            }

            private val tvUp: TextView = itemView.findViewById(R.id.tv_up) as TextView
            private val tvTitle: TextView = itemView.findViewById(R.id.tv_title) as TextView
            private val tvDescription: TextView = itemView.findViewById(R.id.tv_description) as TextView
            private val tvTime: TextView = itemView.findViewById(R.id.tv_time) as TextView

            private val tvChat: TextView = itemView.findViewById(R.id.tv_chat) as TextView

            init {
                tvChat.setOnClickListener {
                    Toast.makeText(context, "click on ${data[adapterPosition].id}", Toast.LENGTH_SHORT).show()
                }
            }

            fun bind(item: Item) {
                tvUp.text = item.score.toString()
                tvTitle.text = item.title
                val urlDes = if (TextUtils.isEmpty(item.url)) "" else " (${getBaseDomain(item.url)})"
                tvDescription.text = "by ${item.byUser}$urlDes"
                tvTime.text = getTimeAgo(item.time)

                if (item.descendants != null && item.descendants > 0) {
                    tvChat.text = "${item.descendants}"
                    tvChat.visibility = View.VISIBLE
                } else {
                    tvChat.visibility = View.GONE
                }
            }

        }

        inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}