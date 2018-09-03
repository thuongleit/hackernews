package me.thuongle.hknews.di

import androidx.work.Worker
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import me.thuongle.hknews.data.worker.FetchStoriesWorker

@Module(subcomponents = [ProfileWorkerModule::class])
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(FetchStoriesWorker::class)
    internal abstract fun bindWorkerFactory(profileWorker: ProfileWorkerModule.Builder): AndroidInjector.Factory<out Worker>
}