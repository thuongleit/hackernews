package me.thuongle.hknews.view.base

interface NetworkView : BaseView {

    fun showNetworkError(t: Throwable)

    fun showInAppError(t: Throwable)
}