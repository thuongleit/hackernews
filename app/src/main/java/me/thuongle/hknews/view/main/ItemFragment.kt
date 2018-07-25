package me.thuongle.hknews.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.thuongle.hknews.R
import me.thuongle.hknews.vo.StoryType
import me.thuongle.hknews.databinding.FragmentItemBinding
import me.thuongle.hknews.di.Injectable
import me.thuongle.hknews.util.autoCleared
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ItemFragment : Fragment(), Injectable {

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

        viewModel.showStory(StoryType.valueOf(storyType))

        val adapter = ItemAdapter(activity!!)
        this.adapter = adapter
        binding.recyclerView.adapter = adapter
        viewModel.stories.observe(this, Observer { stories ->
            adapter.submitList(stories)
        })
        viewModel.networkState.observe(this, Observer { networkState ->
            adapter.setState(networkState)
        })
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