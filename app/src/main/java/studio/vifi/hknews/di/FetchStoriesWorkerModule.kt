package studio.vifi.hknews.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import studio.vifi.hknews.data.worker.FetchStoriesWorker

@Subcomponent
interface ProfileWorkerModule : AndroidInjector<FetchStoriesWorker> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FetchStoriesWorker>()
}