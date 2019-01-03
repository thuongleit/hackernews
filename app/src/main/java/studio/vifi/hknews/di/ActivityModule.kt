package studio.vifi.hknews.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.vifi.hknews.view.main.MainActivity
import studio.vifi.hknews.view.story.StoryActivity


@Module(includes = [ViewModelModule::class])
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeStoryActivity(): StoryActivity
}