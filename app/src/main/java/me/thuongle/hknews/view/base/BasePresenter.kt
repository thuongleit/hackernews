package me.thuongle.hknews.view.base

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
interface BasePresenter {

    fun subscribe()

    fun unsubscribe()
}

