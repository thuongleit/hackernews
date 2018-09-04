package studio.vifi.hknews.view.story

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.vifi.hknews.R
import studio.vifi.hknews.data.vo.Item
import studio.vifi.hknews.databinding.FragmentStoryBinding
import studio.vifi.hknews.di.Injectable
import studio.vifi.hknews.util.autoCleared
import java.lang.IllegalArgumentException
import javax.inject.Inject

class StoryFragment : Fragment(), Injectable {

    private lateinit var viewModel: StoryViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentStoryBinding>()
    var adapter by autoCleared<StoryAdapter>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StoryViewModel::class.java)

        val storyType = arguments?.getString(ARG_TYPE)
                ?: throw IllegalArgumentException("Required argument \"type\" is missing and does not have an android:defaultValue")

        viewModel.loadStories(Item.StoryType.valueOf(storyType))

        val adapter = StoryAdapter(activity!!)
        this.adapter = adapter
        binding.rvStories.adapter = adapter
        viewModel.liveStories.observe(this, Observer { stories ->
            adapter.submitList(stories)
            if (stories != null && stories.isNotEmpty()) {
                this.binding.loading = false
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentStoryBinding>(
                inflater,
                R.layout.fragment_story,
                container,
                false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding.loading = true
    }

    companion object {
        const val ARG_TYPE = "type"
    }
}