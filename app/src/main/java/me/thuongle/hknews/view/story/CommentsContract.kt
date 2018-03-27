package me.thuongle.hknews.view.story

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.thuongle.hknews.api.endpoint.Api
import me.thuongle.hknews.view.base.BasePresenter
import me.thuongle.hknews.view.base.NetworkView

internal interface CommentsContract {

    interface Presenter : BasePresenter

    interface View : NetworkView
}

internal class CommentsPresenterImpl(private val view: CommentsContract.View,
                                     private val api: Api, private val storyId: Long) : CommentsContract.Presenter {

    private var disposable: Disposable? = null

    override fun subscribe() {
        disposable = api.getItemDetailWith("$storyId")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.doOnSubscribe { view. }
                .doOnComplete { }
                .subscribe { }
    }

    override fun unsubscribe() {
        disposable?.dispose()
    }

    companion object {
        private val TAG = "CommentsPresenterImpl"
    }
}