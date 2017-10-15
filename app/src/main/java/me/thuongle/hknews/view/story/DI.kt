package me.thuongle.hknews.view.story

import dagger.Component
import dagger.Module
import dagger.Provides
import me.thuongle.hknews.api.endpoint.Api
import me.thuongle.hknews.di.ApplicationComponent
import me.thuongle.hknews.di.PerFragment

@PerFragment
@Component(modules = arrayOf(StoryModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface StoryComponent {
    fun inject(fragment: StoryContentFragment)
}

@PerFragment
@Component(modules = arrayOf(CommentModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface CommentComponent {
    fun inject(fragment: CommentFragment)
}

@Module
internal class StoryModule(private val view: StoryContract.View, private val contentUrl: String) {

    @Provides
    @PerFragment
    fun provideStoryPresenter(api: Api): StoryContract.Presenter = StoryPresenterImpl(view, api, contentUrl)
}

@Module
internal class CommentModule(private val view: CommentsContract.View, private val storyId: Long) {
    @Provides
    @PerFragment
    fun provideCommentsPresenter(api: Api): CommentsContract.Presenter = CommentsPresenterImpl(view, api, storyId)
}


