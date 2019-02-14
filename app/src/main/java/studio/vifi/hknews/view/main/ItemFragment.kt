package studio.vifi.hknews.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import studio.vifi.hknews.R
import studio.vifi.hknews.Result
import studio.vifi.hknews.databinding.FragmentItemBinding
import studio.vifi.hknews.di.Injectable
import studio.vifi.hknews.model.vo.StoryType
import studio.vifi.hknews.util.autoCleared
import javax.inject.Inject

class ItemFragment : androidx.fragment.app.Fragment(), Injectable {

    private lateinit var viewModel: ItemViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentItemBinding>()
    var adapter by autoCleared<ItemAdapter>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel::class.java)

        val storyType = arguments?.getString(ARG_TYPE)
                ?: throw IllegalArgumentException("Required argument \"type\" is missing and does not have an android:defaultValue")


        val adapter = ItemAdapter(activity!!)
        this.adapter = adapter
        binding.recyclerView.adapter = adapter
        viewModel.result.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    adapter.submitList(result.data)
                }
            }
        })
/*        viewModel.networkState.observe(this, Observer { networkState ->
            adapter.setState(networkState)
        })*/

        viewModel.fetchItems(StoryType.valueOf(storyType))
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
        const val ARG_TYPE = "type"
    }
}