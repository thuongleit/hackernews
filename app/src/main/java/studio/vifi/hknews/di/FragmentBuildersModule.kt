package studio.vifi.hknews.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.vifi.hknews.view.story.StoryFragment
import studio.vifi.hknews.view.item.CommentsFragment
import studio.vifi.hknews.view.item.ContentFragment

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeItemFragment(): StoryFragment

    @ContributesAndroidInjector
    abstract fun contributeContentFragment(): ContentFragment

    @ContributesAndroidInjector
    abstract fun contributeCommentsFragment(): CommentsFragment
}
