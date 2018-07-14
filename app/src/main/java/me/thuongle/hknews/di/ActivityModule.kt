package me.thuongle.hknews.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.thuongle.hknews.view.main.MainActivity
import me.thuongle.hknews.view.story.StoryActivity


@Module(includes = [ViewModelModule::class])
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeStoryActivity(): StoryActivity
}