package me.thuongle.hknews.view.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import me.thuongle.hknews.R
import me.thuongle.hknews.api.model.StoryType
import me.thuongle.hknews.view.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var layoutContent: FrameLayout
    private lateinit var navigation: BottomNavigationView

    private var isInStoryMode: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutContent = findViewById(R.id.content) as FrameLayout

        navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_id_1
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_change_story -> {
                val navigation1 = navigation.menu.findItem(R.id.navigation_id_1)
                val navigation2 = navigation.menu.findItem(R.id.navigation_id_2)
                val navigation3 = navigation.menu.findItem(R.id.navigation_id_3)

                if (isInStoryMode) {
                    isInStoryMode = false
                    item.setIcon(R.drawable.ic_story_menu)
                    navigation1.setIcon(R.drawable.ic_ask)
                    navigation1.setTitle(R.string.title_ask)
                    navigation2.setIcon(R.drawable.ic_show)
                    navigation2.setTitle(R.string.title_show)
                    navigation3.setIcon(R.drawable.ic_job)
                    navigation3.setTitle(R.string.title_job)
                } else {
                    isInStoryMode = true
                    item.setIcon(R.drawable.ic_show_presenter)
                    navigation1.setIcon(R.drawable.ic_top)
                    navigation1.setTitle(R.string.title_top)
                    navigation2.setIcon(R.drawable.ic_new)
                    navigation2.setTitle(R.string.title_new)
                    navigation3.setIcon(R.drawable.ic_best)
                    navigation3.setTitle(R.string.title_best)
                }
                navigation.selectedItemId = R.id.navigation_id_1
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val storyType: StoryType? = when (item.itemId) {
            R.id.navigation_id_1 -> if (isInStoryMode) StoryType.TOP else StoryType.ASK
            R.id.navigation_id_2 -> if (isInStoryMode) StoryType.NEW else StoryType.SHOW
            R.id.navigation_id_3 -> if (isInStoryMode) StoryType.BEST else StoryType.JOB
            else -> null
        }

        if (storyType != null) {
            val fragment: Fragment = MainActivityFragment.newInstance(storyType)
            supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
            return@OnNavigationItemSelectedListener true
        }

        false
    }
}
