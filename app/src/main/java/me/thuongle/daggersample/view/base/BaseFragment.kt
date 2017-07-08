package me.thuongle.daggersample.view.base

import android.support.v4.app.Fragment
import me.thuongle.daggersample.di.ApplicationComponent

abstract class BaseFragment : Fragment() {

    val component: ApplicationComponent
        get() = (activity as BaseActivity).component

    protected var presenter: BasePresenter? = null

    override fun onStart() {
        super.onStart()
            presenter?.subscribe()
    }

    override fun onStop() {
        super.onStop()
            presenter?.unsubscribe()
    }

    companion object {
        val TAG = "BaseFragment"
    }
}
