package me.thuongle.daggersample.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.thuongle.daggersample.R
import me.thuongle.daggersample.api.model.StoryType
import me.thuongle.daggersample.api.model.Item
import me.thuongle.daggersample.util.getBaseDomain
import me.thuongle.daggersample.util.getTimeAgo
import me.thuongle.daggersample.view.base.BaseFragment
import javax.inject.Inject
import android.widget.Toast
import me.thuongle.daggersample.view.base.BasePresenter


class MainActivityFragment : BaseFragment(), MainContract.View {

    @Inject internal
    lateinit var presenter: MainContract.Presenter

    override fun getPresenter(): BasePresenter? = presenter

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

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ItemRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun showNetworkError() {
    }

    override fun showInAppError() {
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

    private class ItemRecyclerViewAdapter(val context: Context) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {
        val data: MutableList<Item> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder
                = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int = data.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

            fun bind(item: Item) {
                tvUp.text = item.score.toString()
                tvTitle.text = item.title
                tvDescription.text = "by ${item.byUser} (${getBaseDomain(item.url)})"
                tvTime.text = getTimeAgo(item.time)
            }
        }

        fun add(item: Item) {
            data.add(item)
            notifyItemInserted(data.size - 1)
        }
    }
}

