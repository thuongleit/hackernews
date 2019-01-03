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
import studio.vifi.hknews.databinding.FragmentCommentBinding
import studio.vifi.hknews.di.Injectable
import studio.vifi.hknews.repository.LOADED
import studio.vifi.hknews.repository.LOADING
import studio.vifi.hknews.util.autoCleared
import javax.inject.Inject

class CommentsFragment : Fragment(), Injectable {

    private lateinit var viewModel: ItemCommentViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentCommentBinding>()
    private var adapter by autoCleared<CommentItemAdapter>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemCommentViewModel::class.java)

        val storyId = CommentsFragmentArgs.fromBundle(arguments).id
                ?: throw IllegalArgumentException("Required argument \"id\" is missing and does not have an android:defaultValue")

        viewModel.setId(storyId.toLong())

        val adapter = CommentItemAdapter().also {
            this.adapter = it
        }
        binding.recyclerView.adapter = adapter
        viewModel.comments.observe(this, Observer { comments ->
            adapter.submitList(comments)
        })

        viewModel.networkState.observe(this, Observer { networkState ->
            binding.loading = when (networkState) {
                is LOADED -> false
                is LOADING -> true
                else -> false
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentCommentBinding>(
                inflater,
                R.layout.fragment_comment,
                container,
                false
        )
        this.binding = binding
        return binding.root
    }

    companion object {
        private const val TAG = "CommentsFragment"
        const val ARG_STORY_ID = "id"
    }
}