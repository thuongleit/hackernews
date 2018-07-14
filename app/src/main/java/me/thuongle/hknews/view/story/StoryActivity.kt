package me.thuongle.hknews.view.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_story.*
import me.thuongle.hknews.R
import me.thuongle.hknews.api.Item
import me.thuongle.hknews.util.bundleOf
import java.lang.IllegalArgumentException
import javax.inject.Inject

class StoryActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        val storyUrl = intent.getStringExtra(EXTRA_STORY_URL)
                ?: throw IllegalArgumentException("Required argument \"storyUrl\" is missing")
        val storyId = intent.getLongExtra(EXTRA_STORY_ID, 0)
                ?: throw IllegalArgumentException("Required argument \"storyId\" is missing")
        val navigationToComment = intent.getBooleanExtra(EXTRA_GO_TO_COMMENT, false)

        val navController = findNavController(R.id.nav_fragment)
        bottom_navigation.setupWithNavController(navController)

        //re-setup listener for our use
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            val builder = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            builder.setPopUpTo(navController.graph.startDestination, false)
            val options = builder.build()
            return@setOnNavigationItemSelectedListener try {
                val (direction, args) =
                        when (item.itemId) {
                            R.id.contentFragment -> {
                                Pair(R.id.contentFragment,
                                        bundleOf(ContentFragment.ARG_STORY_URL to storyUrl))
                            }
                            R.id.commentsFragment -> {
                                Pair(R.id.commentsFragment,
                                        bundleOf(CommentsFragment.ARG_STORY_ID to storyId.toString()))
                            }
                            else -> throw IllegalArgumentException("This fragment doesn't contains menuItem Id ${item.itemId}")
                        }
                navController.navigate(direction, args, options)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }

        bottom_navigation.selectedItemId = if (navigationToComment) R.id.commentsFragment else R.id.contentFragment
    }

    companion object {
        private const val EXTRA_STORY_ID = "hknews.extra.STORY_ID"
        private const val EXTRA_STORY_URL = "hknews.extra.STORY_URL"
        private const val EXTRA_GO_TO_COMMENT = "hknews.extra.GO_TO_COMMENT"

        fun newInstance(context: Context, item: Item, goToComments: Boolean = false): Intent {
            return Intent(context, StoryActivity::class.java)
                    .apply {
                        putExtra(EXTRA_STORY_ID, item.id)
                        putExtra(EXTRA_STORY_URL, item.url)
                        putExtra(EXTRA_GO_TO_COMMENT, goToComments)
                    }
        }
    }
}
