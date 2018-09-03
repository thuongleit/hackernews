package me.thuongle.hknews.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import me.thuongle.hknews.data.worker.FetchStoriesWorker

@Subcomponent
interface ProfileWorkerModule : AndroidInjector<FetchStoriesWorker> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FetchStoriesWorker>()
}