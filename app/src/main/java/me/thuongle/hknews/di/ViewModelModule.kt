package me.thuongle.hknews.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.thuongle.hknews.view.ViewModelFactory
import me.thuongle.hknews.view.main.StoryViewModel
import me.thuongle.hknews.view.story.ItemCommentViewModel

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
