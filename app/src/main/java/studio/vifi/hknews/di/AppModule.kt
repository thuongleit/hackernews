package studio.vifi.hknews.di

import dagger.Module
import dagger.Provides
import studio.vifi.hknews.AppExecutors

@Module
class AppModule {
    @Provides
    fun provideAppExecutors(): AppExecutors {
        return AppExecutors()
    }
}
