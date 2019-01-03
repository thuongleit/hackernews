package studio.vifi.hknews.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.vifi.hknews.view.main.ItemFragment
import studio.vifi.hknews.view.story.CommentsFragment
import studio.vifi.hknews.view.story.ContentFragment

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeItemFragment(): ItemFragment

    @ContributesAndroidInjector
    abstract fun contributeContentFragment(): ContentFragment

    @ContributesAndroidInjector
    abstract fun contributeCommentsFragment(): CommentsFragment
}
