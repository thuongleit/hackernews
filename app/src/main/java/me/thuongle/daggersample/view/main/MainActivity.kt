package me.thuongle.daggersample.view.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import me.thuongle.daggersample.R
import me.thuongle.daggersample.api.model.StoryType
import me.thuongle.daggersample.view.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var layoutContent: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutContent = findViewById(R.id.content) as FrameLayout

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_newest
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment?

        when (item.itemId) {
            R.id.navigation_newest -> fragment = MainActivityFragment.newInstance(StoryType.NEW)
            R.id.navigation_top -> fragment = MainActivityFragment.newInstance(StoryType.TOP)
            R.id.navigation_best -> fragment = MainActivityFragment.newInstance(StoryType.BEST)
            else -> fragment = null
        }

        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
            return@OnNavigationItemSelectedListener true
        }

        false
    }
}
