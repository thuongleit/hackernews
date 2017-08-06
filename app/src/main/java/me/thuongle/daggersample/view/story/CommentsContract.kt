package me.thuongle.daggersample.view.story

import io.reactivex.disposables.Disposable
import me.thuongle.daggersample.api.endpoint.Api
import me.thuongle.daggersample.view.base.BasePresenter
import me.thuongle.daggersample.view.base.NetworkView

internal interface CommentsContract {

    interface Presenter : BasePresenter

    interface View : NetworkView
}

internal class CommentsPresenterImpl(private val view: CommentsContract.View,
                                     private val api: Api, private val storyId: Long) : CommentsContract.Presenter {

    private var disposable: Disposable? = null

    override fun subscribe() {
    }

    override fun unsubscribe() {
        disposable?.dispose()
    }

    companion object {
        private val TAG = "CommentsPresenterImpl"
    }
}