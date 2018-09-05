package studio.vifi.hknews.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import studio.vifi.hknews.view.ViewModelFactory
import studio.vifi.hknews.view.item.ItemViewModel
import studio.vifi.hknews.view.detail.ItemCommentViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ItemViewModel::class)
    abstract fun bindItemViewModel(itemViewModel: ItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemCommentViewModel::class)
    abstract fun bindCommentViewModel(viewModel: ItemCommentViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
