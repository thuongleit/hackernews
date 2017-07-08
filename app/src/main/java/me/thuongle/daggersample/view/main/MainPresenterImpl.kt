package me.thuongle.daggersample.view.main

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.thuongle.daggersample.api.endpoint.Api
import me.thuongle.daggersample.api.model.StoryType

internal class MainPresenterImpl(private val view: MainContract.View,
                                 private val api: Api, private val storyType: Int) : MainContract.Presenter {

    private var disposable: Disposable? = null

    override fun subscribe() {
        val requestApi: Flowable<ArrayList<Long>>?

        when (storyType) {
            StoryType.NEW.ordinal -> requestApi = api.getNewStories()
            StoryType.TOP.ordinal -> requestApi = api.getTopStories()
            StoryType.BEST.ordinal -> requestApi = api.getBestStories()
            else -> requestApi = null
        }

        requestApi ?: return

        disposable = requestApi
                //.flatMap { Flowable.fromIterable(it) }
                .flatMapIterable { it }
                .doOnNext { Log.d(TAG, "received item id: $it") }
                .flatMap { api.getItemDetailWith(id = it.toString()) }
                .filter { !(it.dead && it.deleted) }
                .take(LIMIT.toLong())
                //.toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    run {
                        view.onReceiveData(item)
                    }
                }
    }

    override fun unsubscribe() {
        disposable?.dispose()
    }

    companion object {
        private val TAG = "MainPresenterImpl"
        val LIMIT = 30
    }
}