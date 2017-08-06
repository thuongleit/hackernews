package me.thuongle.daggersample

import dagger.Module
import dagger.Provides
import me.thuongle.daggersample.di.Interceptor
import javax.inject.Singleton

@Module
class DebugModule {

    @Provides
    @Singleton
    @Interceptor
    internal fun provideInterceptors(): Array<okhttp3.Interceptor> {
        return arrayOf()
    }
}
