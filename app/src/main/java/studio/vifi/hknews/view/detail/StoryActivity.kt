package studio.vifi.hknews.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import studio.vifi.hknews.R
import studio.vifi.hknews.data.model.Item
import javax.inject.Inject

class StoryActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

    }

    companion object {
        private const val TAG = "StoryActivity"

        private const val EXTRA_ITEM = "hknews.extra.ITEM"
        private const val EXTRA_GO_TO_COMMENT_TAB = "hknews.extra.GO_TO_COMMENT_TAB"

        fun newInstance(context: Context, item: Item, goToCommentTab: Boolean = false): Intent {
            val intent = Intent(context, StoryActivity::class.java)
            intent.putExtra(EXTRA_GO_TO_COMMENT_TAB, goToCommentTab)

            return intent
        }
    }
}
