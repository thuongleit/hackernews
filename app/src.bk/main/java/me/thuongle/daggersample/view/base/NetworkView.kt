package me.thuongle.daggersample.view.base

interface NetworkView : BaseView {

    fun showNetworkError(t: Throwable)

    fun showInAppError(t: Throwable)
}