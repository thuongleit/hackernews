package me.thuongle.hknews.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.thuongle.hknews.view.main.StoryFragment
import me.thuongle.hknews.view.story.CommentsFragment
import me.thuongle.hknews.view.story.ContentFragment

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeItemFragment(): StoryFragment

    @ContributesAndroidInjector
    abstract fun contributeContentFragment(): ContentFragment

    @ContributesAndroidInjector
    abstract fun contributeCommentsFragment(): CommentsFragment
}
