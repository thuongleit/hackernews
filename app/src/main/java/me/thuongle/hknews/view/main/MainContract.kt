package me.thuongle.hknews.view.main

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.thuongle.hknews.api.endpoint.Api
import me.thuongle.hknews.api.model.Item
import me.thuongle.hknews.api.model.StoryType
import me.thuongle.hknews.view.base.BasePresenter
import me.thuongle.hknews.view.base.NetworkView
import java.io.IOException

internal interface MainContract {

    interface Presenter : BasePresenter {

        fun loadMore()
        fun reload()
    }

    interface View : NetworkView {

        fun onReceiveData(item: Item)
        fun onError(t: Throwable)
        fun showProgress(show: Boolean)
        fun removeNetworkErrorLayoutIfNeeded()
    }
}

internal class MainPresenterImpl(private val view: MainContract.View,
                                 private val api: Api, private val storyType: Int) : MainContract.Presenter {

    private var disposable: Disposable? = null

    private var allStoryIds: ArrayList<Long>? = null
    private val takenStoryIds: ArrayList<Long> = ArrayList()
    var isLoading: Boolean = false

    override fun subscribe() {
        disposable =
                Flowable.just(takenStoryIds.isEmpty())
                        .takeWhile { it }
                        .flatMap { executeApiRequest() }
                        .doOnSubscribe {
                            view.showProgress(true)
                            view.removeNetworkErrorLayoutIfNeeded()
                        }
                        .doOnNext { view.showProgress(false) }
                        .doOnComplete { view.showProgress(false) }
                        .subscribe { view.onReceiveData(it) }
    }

    override fun loadMore() {
        Log.d(TAG, "loadMore")
        disposable = executeApiRequest().subscribe { view.onReceiveData(it) }
    }

    override fun reload() {
        resetVariables()
        subscribe()
    }

    private fun executeApiRequest(): Flowable<Item> {
        return getItemEmitter()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { isLoading = true }
                .doOnNext { if (allStoryIds == null) allStoryIds = it }
                .flatMapIterable { it }
                .take(LIMIT_ITEM)
                .doOnNext {
                    Log.d(TAG, "took story with id: $it")
                    takenStoryIds.add(it)
                }
                .flatMap { api.getItemDetailWith(id = it.toString()) }
                .filter { !(it.dead && it.deleted) }
                .doOnComplete {
                    isLoading = false
                    allStoryIds?.removeAll(takenStoryIds)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { it: Throwable ->
                    Log.e(TAG, it.message, it)

                    view.showProgress(false)
                    if (takenStoryIds.isEmpty()) {
                        if (it is IOException) {
                            view.showNetworkError(it)
                        } else {
                            view.showInAppError(it)
                        }
                    }

                    Flowable.empty()
                }
    }

    override fun unsubscribe() {
        disposable?.dispose()
    }

    private fun getItemEmitter(): Flowable<ArrayList<Long>> {
        if (allStoryIds != null) {
            return Flowable.just(allStoryIds)
        }

        return when (storyType) {
            StoryType.NEW.ordinal -> api.getNewStories()
            StoryType.TOP.ordinal -> api.getTopStories()
            StoryType.BEST.ordinal -> api.getBestStories()
            StoryType.ASK.ordinal -> api.getAskstories()
            StoryType.SHOW.ordinal -> api.getShowstories()
            StoryType.JOB.ordinal -> api.getJobstories()
            else -> Flowable.empty()
        }
    }

    private fun resetVariables() {
        allStoryIds = null
        takenStoryIds.clear()
    }

    companion object {
        private val TAG = "MainPresenterImpl"
        val LIMIT_ITEM: Long = 30
        val LOADING_VISIBLE_THRESHOLD = 5
    }
}