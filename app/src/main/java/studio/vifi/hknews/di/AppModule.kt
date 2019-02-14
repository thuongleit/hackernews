package studio.vifi.hknews.di

import dagger.Module
import dagger.Provides
import studio.vifi.hknews.AppExecutors
import studio.vifi.hknews.model.api.ApiService
import studio.vifi.hknews.model.repository.ItemRepository
import studio.vifi.hknews.model.repository.ItemRepositoryImpl
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideItemRepository(service: ApiService, executors: AppExecutors): ItemRepository
            = ItemRepositoryImpl(service, executors)
}
