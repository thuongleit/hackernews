package studio.vifi.hknews.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_item.*
import studio.vifi.hknews.R
import studio.vifi.hknews.Result
import studio.vifi.hknews.databinding.FragmentItemBinding
import studio.vifi.hknews.di.Injectable
import studio.vifi.hknews.isConnected
import studio.vifi.hknews.model.vo.StoryType
import studio.vifi.hknews.util.autoCleared
import studio.vifi.hknews.view.common.Callback
import javax.inject.Inject

class ItemFragment : androidx.fragment.app.Fragment(), Injectable {

    private lateinit var viewModel: ItemViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentItemBinding>()
    var adapter by autoCleared<ItemAdapter>()

    private lateinit var storyType: StoryType

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel::class.java)

        val type = arguments?.getString(ARG_TYPE)
                ?: throw IllegalArgumentException("Required argument \"type\" is missing and does not have an android:defaultValue")
        storyType = StoryType.valueOf(type)
        val adapter = ItemAdapter(activity!!)
        this.adapter = adapter
        recycler_view.adapter = adapter
        observeToViewModel(viewModel)

        viewModel.fetchItems(storyType)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentItemBinding>(
                inflater,
                R.layout.fragment_item,
                container,
                false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        this.binding.retryCallback = object : Callback {
            override fun invoke(v: View) {
                viewModel.fetchItems(storyType, force = true)
            }
        }
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                if (viewModel.canLoadMore()) {
                    val itemCount = recycler_view.layoutManager?.itemCount ?: 0
                    val lastVisibleItemPosition = (recycler_view.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                    if (!adapter.isLoading() && (lastVisibleItemPosition + ItemAdapter.VISIBLE_THRESHOLD) >= itemCount) {
                        viewModel.loadMore()
                    }
                }

                super.onScrolled(view, dx, dy)
            }
        })
        swipe_to_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeToViewModel(viewModel: ItemViewModel) {
        viewModel.result.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    adapter.submitList(result.data)
                    binding.loading = false
                    binding.failed = false
                }
                is Result.Running -> {
                    binding.loading = true
                    binding.failed = false
                }
                is Result.Failure -> {
                    binding.loading = false
                    binding.failed = true
                    if (context?.isConnected() == false) {
                        binding.errorMsg = getString(R.string.error_connection_lost_message)
                    } else {
                        binding.errorMsg = result.exception.message ?: "Unknown error"
                    }
                }
            }
        })

        viewModel.nextPageLoadingStatus.observe(this, Observer {
            val isLoading = it is Result.Running
            adapter.setLoading(isLoading)
            binding.loadingNextPage = isLoading
        })
        viewModel.refreshStatus.observe(this, Observer {
            swipe_to_refresh.isRefreshing = (it is Result.Running)
        })
    }

    companion object {
        const val ARG_TYPE = "type"
    }
}