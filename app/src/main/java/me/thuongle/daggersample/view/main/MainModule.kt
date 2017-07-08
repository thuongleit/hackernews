package me.thuongle.daggersample.view.main

import me.thuongle.daggersample.api.endpoint.Api
import dagger.Module
import dagger.Provides
import me.thuongle.daggersample.di.PerFragment

@Module
internal class MainModule(private val view: MainContract.View, private val storyType: Int) {

    @Provides
    @PerFragment
    fun providePresenter(api: Api): MainContract.Presenter
            = MainPresenterImpl(view, api, storyType)
}