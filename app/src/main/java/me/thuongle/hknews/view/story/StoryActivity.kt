package me.thuongle.hknews.view.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.widget.FrameLayout
import me.thuongle.hknews.R
import me.thuongle.hknews.view.base.BaseActivity

class StoryActivity : BaseActivity() {

    private lateinit var layoutContent: FrameLayout
    private lateinit var navigation: BottomNavigationView

    private var storyId: Long = 0L
    private lateinit var originUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        originUrl = intent.getStringExtra(EXTRA_ORIGIN_URL)
        storyId = intent.getLongExtra(EXTRA_STORY_ID, 0)

        layoutContent = findViewById(R.id.content) as FrameLayout

        navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (TextUtils.isEmpty(originUrl)) {
            navigation.menu.removeItem(R.id.navigation_content)
        }

        if (intent.getBooleanExtra(EXTRA_GO_TO_COMMENT, false)) {
            navigation.selectedItemId = R.id.navigation_comments
        } else {
            navigation.selectedItemId = R.id.navigation_content
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment?

        when (item.itemId) {
            R.id.navigation_content -> fragment = StoryContentFragment.newInstance(originUrl)
            R.id.navigation_comments -> fragment = CommentFragment.newInstance(storyId)
            else -> fragment = null
        }

        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
            return@OnNavigationItemSelectedListener true
        }

        false
    }

    companion object {
        val EXTRA_STORY_ID = "hknews.extra.STORY_ID"
        val EXTRA_ORIGIN_URL = "hknews.extra.ORIGIN_URL"
        val EXTRA_GO_TO_COMMENT = "hknews.extra.GO_TO_COMMENT"

        fun newInstance(context: Context, storyId: Long, originUrl: String? = "", goToComments: Boolean = false): Intent {
            val intent = Intent(context, StoryActivity::class.java)
            intent.putExtra(EXTRA_STORY_ID, storyId)
            intent.putExtra(EXTRA_ORIGIN_URL, originUrl)
            intent.putExtra(EXTRA_GO_TO_COMMENT, goToComments)

            return intent
        }
    }
}
