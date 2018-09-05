package studio.vifi.hknews.view.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_item.*
import studio.vifi.hknews.R
import studio.vifi.hknews.data.model.StoryType
import studio.vifi.hknews.data.repository.LOADING
import studio.vifi.hknews.data.repository.RequestType
import studio.vifi.hknews.databinding.FragmentItemBinding
import studio.vifi.hknews.di.Injectable
import studio.vifi.hknews.util.autoCleared
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ItemFragment : Fragment(), Injectable {

    private lateinit var model: ItemViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentItemBinding>()
    var adapter by autoCleared<ItemAdapter>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel::class.java)

        val storyType = arguments?.getString(ARG_REQUEST_TYPE)
                ?: throw IllegalArgumentException("Required argument \"requestType\" is missing and does not have an android:defaultValue")

        model.requestItems(StoryType.valueOf(storyType))

        val adapter = ItemAdapter(activity!!)
        this.adapter = adapter
        binding.recyclerView.adapter = adapter
        model.liveItems.observe(this, Observer { stories ->
            adapter.submitList(stories)
        })
        model.liveNetworkState.observe(this, Observer { networkState ->
            when (networkState?.requestType) {
                RequestType.INITIAL_LOAD -> {
                }
                RequestType.REFRESH -> swipe_refresh.isRefreshing = (networkState is LOADING)
                RequestType.LOAD_MORE -> {
                    adapter.setState(networkState)
                }
            }
        })
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
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

    companion object {
        const val ARG_REQUEST_TYPE = "type"
    }
}