package me.thuongle.daggersample.view.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.thuongle.daggersample.App
import me.thuongle.daggersample.di.ApplicationComponent

abstract class BaseActivity : AppCompatActivity() {

    val app: App
        get() = application as App

    val component: ApplicationComponent
        get() = app.applicationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        getPresenter()?.subscribe()
    }

    override fun onStop() {
        super.onStop()
        getPresenter()?.unsubscribe()
    }

    protected open fun getPresenter() : BasePresenter? = null

    fun reload() {
        overridePendingTransition(0, 0)
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }
}
