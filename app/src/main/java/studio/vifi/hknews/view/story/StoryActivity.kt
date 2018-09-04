package studio.vifi.hknews.view.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_story.*
import studio.vifi.hknews.R
import java.lang.IllegalArgumentException
import javax.inject.Inject

class StoryActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        val storyId = intent.getLongExtra(EXTRA_ITEM_ID, 0L)
                ?: throw IllegalArgumentException("Required argument \"item\" is missing")
//        title = storyId.title

        try {
            //start enter activity transaction on toolbar title
            val actionbar = findViewById<Toolbar>(R.id.action_bar)
            val tvTitle = actionbar.getChildAt(0)
            ViewCompat.setTransitionName(tvTitle, SHARED_VIEW_TOOLBAR_TITLE)
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

        val navController = findNavController(R.id.nav_fragment)
        bottom_navigation.setupWithNavController(navController)

        //re-setup listener for our use
//        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
//            val builder = NavOptions.Builder()
//                    .setLaunchSingleTop(true)
//                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
//                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
//                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
//                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
//            builder.setPopUpTo(navController.graph.startDestination, false)
//            val options = builder.build()
//            return@setOnNavigationItemSelectedListener try {
//                val (direction, args) =
//                        when (menuItem.itemId) {
//                            R.id.contentFragment -> {
//                                Pair(R.id.contentFragment,
//                                        bundleOf(ContentFragment.ARG_STORY_URL to ("storyId.url"
//                                                ?: "")))
//                            }
//                            R.id.commentsFragment -> {
//                                Pair(R.id.commentsFragment,
//                                        bundleOf(CommentsFragment.ARG_STORY_ID to storyId.id.toString()))
//                            }
//                            else -> throw IllegalArgumentException("This fragment doesn't contains menuItem Id ${menuItem.itemId}")
//                        }
//                navController.navigate(direction, args, options)
//                true
//            } catch (e: IllegalArgumentException) {
//                false
//            }
//        }

//        val navigationToComment = intent.getBooleanExtra(EXTRA_GO_TO_COMMENT_TAB, false)
//        bottom_navigation.selectedItemId = if (navigationToComment || storyId.url.isNullOrBlank()) {
//            R.id.commentsFragment
//        } else {
//            R.id.contentFragment
//        }
        //bottom_navigation.visibility = if (storyId.url.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    override fun onBackPressed() {
        finish()
    }
    companion object {
        private const val TAG = "StoryActivity"
        const val SHARED_VIEW_TOOLBAR_TITLE = "toolbar:title"

        private const val EXTRA_ITEM_ID = "hknews.extra.ITEM_ID"
        private const val EXTRA_GO_TO_COMMENT_TAB = "hknews.extra.GO_TO_COMMENT_TAB"

        fun newInstance(context: Context, itemId: Long, goToCommentTab: Boolean = false): Intent {
            val intent = Intent(context, StoryActivity::class.java)
            intent.putExtra(EXTRA_ITEM_ID, itemId)
            intent.putExtra(EXTRA_GO_TO_COMMENT_TAB, goToCommentTab)

            return intent
        }
    }
}
