package me.thuongle.hknews.view.main

import dagger.Component
import dagger.Module
import dagger.Provides
import me.thuongle.hknews.api.endpoint.Api
import me.thuongle.hknews.di.ApplicationComponent
import me.thuongle.hknews.di.PerFragment

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