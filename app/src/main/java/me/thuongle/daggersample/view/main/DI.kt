package me.thuongle.daggersample.view.main

import dagger.Component
import dagger.Module
import dagger.Provides
import me.thuongle.daggersample.api.endpoint.Api
import me.thuongle.daggersample.di.ApplicationComponent
import me.thuongle.daggersample.di.PerFragment

@PerFragment
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface MainComponent {

    fun inject(fragment: MainActivityFragment)
}

@Module
internal class MainModule(private val view: MainContract.View, private val storyType: Int) {

    @Provides
    @PerFragment
    fun providePresenter(api: Api): MainContract.Presenter = MainPresenterImpl(view, api, storyType)
}