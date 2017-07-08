package me.thuongle.daggersample.view.main

import dagger.Component
import me.thuongle.daggersample.di.ApplicationComponent
import me.thuongle.daggersample.di.PerFragment

@PerFragment
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface MainComponent {

    fun inject(fragment: MainActivityFragment)
}