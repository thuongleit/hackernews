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
    private var requestApi: Flowable<ArrayList<Long>>? = null
    var isLoading: Boolean = false
    var stories: ArrayList<Long>? = null
    private val loadedData: ArrayList<Long> = ArrayList()

    override fun subscribe() {
        when (storyType) {
            StoryType.NEW.ordinal -> requestApi = api.getNewStories()
            StoryType.TOP.ordinal -> requestApi = api.getTopStories()
            StoryType.BEST.ordinal -> requestApi = api.getBestStories()
            else -> requestApi = null
        }

        requestApi ?: return

        disposable = executeRequest()
    }

    override fun loadMore() {
        Log.d(TAG, "request loadMore")

        if (stories != null) {
            requestApi = Flowable.just(stories)
        }

        disposable = executeRequest()
    }

    private fun executeRequest(): Disposable? {
        return requestApi!!
                .doOnNext { stories = it }
                .flatMapIterable { it }
                .doOnNext {
                    loadedData.add(it)
                    Log.d(TAG, "received story id: $it")
                }
                .flatMap { api.getItemDetailWith(id = it.toString()) }
                .filter { !(it.dead && it.deleted) }
                .take(LIMIT_ITEM)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    isLoading = true
                    loadedData.clear()
                }
                .doOnComplete {
                    isLoading = false
                    stories?.removeAll(loadedData)
                }
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
        val LIMIT_ITEM: Long = 30
        val LOADING_VISIBLE_THRESHOLD = 5
    }
}