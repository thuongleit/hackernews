package me.thuongle.daggersample.view.story

import io.reactivex.disposables.Disposable
import me.thuongle.daggersample.api.endpoint.Api
import me.thuongle.daggersample.view.base.BasePresenter
import me.thuongle.daggersample.view.base.NetworkView

internal interface StoryContract {

    interface Presenter : BasePresenter

    interface View : NetworkView
}

internal class StoryPresenterImpl(private val view: StoryContract.View,
                                  private val api: Api, private val contentUrl: String) : StoryContract.Presenter {

    private var disposable: Disposable? = null

    override fun subscribe() {
    }

    override fun unsubscribe() {
        disposable?.dispose()
    }

    companion object {
        private val TAG = "StoryPresenterImpl"
    }
}