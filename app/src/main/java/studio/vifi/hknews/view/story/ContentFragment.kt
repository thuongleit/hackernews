package studio.vifi.hknews.view.story

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import studio.vifi.hknews.R
import studio.vifi.hknews.databinding.FragmentStoryContentBinding
import studio.vifi.hknews.di.Injectable
import studio.vifi.hknews.util.autoCleared

class ContentFragment : Fragment(), Injectable {

    private var binding by autoCleared<FragmentStoryContentBinding>()
    private var webViewClient by autoCleared<WebViewClient>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentStoryContentBinding>(
                inflater,
                R.layout.fragment_story_content,
                container,
                false
        )
        this.binding = binding
        this.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.loading = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.loading = false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wvContent.webViewClient = webViewClient
        binding.url = ContentFragmentArgs.fromBundle(arguments).url
    }

    companion object {
        const val ARG_STORY_URL = "url"
    }
}