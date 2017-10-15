package me.thuongle.hknews.view.base

import android.support.v4.app.Fragment
import me.thuongle.hknews.di.ApplicationComponent

abstract class BaseFragment : Fragment() {

    val component: ApplicationComponent
        get() = (activity as BaseActivity).component

    override fun onStart() {
        super.onStart()
            getPresenter()?.subscribe()
    }

    override fun onStop() {
        super.onStop()
            getPresenter()?.unsubscribe()
    }

    protected open fun getPresenter() : BasePresenter? = null

    companion object {
        val TAG = "BaseFragment"
    }
}
