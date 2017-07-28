package me.thuongle.daggersample.view.main

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.thuongle.daggersample.api.endpoint.Api
import me.thuongle.daggersample.api.model.StoryType
import java.io.IOException

internal class MainPresenterImpl(private val view: MainContract.View,
                                 private val api: Api, private val storyType: Int) : MainContract.Presenter {

    private var disposable: Disposable? = null
    private lateinit var flowableOfItems: Flowable<ArrayList<Long>>
    private var allStories: ArrayList<Long>? = null
    private val showedStories: ArrayList<Long> = ArrayList()

    var isLoading: Boolean = false

    override fun subscribe() {
        when (storyType) {
            StoryType.NEW.ordinal -> flowableOfItems = api.getNewStories()
            StoryType.TOP.ordinal -> flowableOfItems = api.getTopStories()
            StoryType.BEST.ordinal -> flowableOfItems = api.getBestStories()
            StoryType.ASK.ordinal -> flowableOfItems = api.getAskstories()
            StoryType.SHOW.ordinal -> flowableOfItems = api.getShowstories()
            StoryType.JOB.ordinal -> flowableOfItems = api.getJobstories()
            else -> flowableOfItems = Flowable.empty()
        }

        disposable = executeApiRequest()
    }

    override fun loadMore() {
        Log.d(TAG, "request loadMore")

        if (allStories != null) {
            flowableOfItems = Flowable.just(allStories)
        }

        disposable = executeApiRequest()
    }

    private fun executeApiRequest(): Disposable? {
        return flowableOfItems
                .subscribeOn(Schedulers.io())
                .doOnNext { if (allStories == null) allStories = it }
                .flatMapIterable { it }
                .doOnNext {
                    showedStories.add(it)
                    Log.d(TAG, "received story id: $it")
                }
                .flatMap { api.getItemDetailWith(id = it.toString()) }
                .filter { !(it.dead && it.deleted) }
                //.onErrorResumeNext { t: Throwable -> Log.e(TAG, "Failed to load item with id $t"); Flowable.empty<Item>() }
                .take(LIMIT_ITEM)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    isLoading = true
                    showedStories.clear()
                }
                .doOnComplete {
                    isLoading = false
                    allStories?.removeAll(showedStories)
                }
                .subscribe(
                        { view.onReceiveData(it) },
                        { t -> if (t is IOException) view.showNetworkError(t) else view.showInAppError(t) }
                )
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