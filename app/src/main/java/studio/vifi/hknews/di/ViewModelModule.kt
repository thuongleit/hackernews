package studio.vifi.hknews.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import studio.vifi.hknews.view.ViewModelFactory
import studio.vifi.hknews.view.main.StoryViewModel
import studio.vifi.hknews.view.story.ItemCommentViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StoryViewModel::class)
    abstract fun bindItemViewModel(itemViewModel: StoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemCommentViewModel::class)
    abstract fun bindCommentViewModel(viewModel: ItemCommentViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
