package studio.vifi.hknews.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import studio.vifi.hknews.data.api.ApiService
import studio.vifi.hknews.data.api.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkLoggerModule::class])
class NetworkModule {

    @Singleton @Provides
    fun provideOkHttpClient(@NetworkInterceptor loggingInterceptors:
                            Set<@JvmSuppressWildcards okhttp3.Interceptor>): OkHttpClient {
        return OkHttpClient.Builder()
                .apply {
                    loggingInterceptors.forEach {
                        addNetworkInterceptor(it)
                    }
                }.build()
    }

    @Singleton @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
    }

    @Singleton @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
