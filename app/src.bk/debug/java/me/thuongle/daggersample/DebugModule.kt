package me.thuongle.daggersample

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import me.thuongle.daggersample.di.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class DebugModule {

    @Provides
    @Singleton
    @Interceptor
    internal fun provideInterceptors(): Array<okhttp3.Interceptor> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return arrayOf(StethoInterceptor(), loggingInterceptor)
    }
}
