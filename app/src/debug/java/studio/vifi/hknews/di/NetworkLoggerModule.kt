package studio.vifi.hknews.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class NetworkLoggerModule {

    @NetworkInterceptor @Provides @Singleton @IntoSet
    fun provideStetho(): okhttp3.Interceptor = StethoInterceptor()

    @NetworkInterceptor @Provides @Singleton @IntoSet
    fun provideNetworkLogger(): okhttp3.Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
}
