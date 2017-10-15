package me.thuongle.hknews.api

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.thuongle.hknews.api.endpoint.Api
import me.thuongle.hknews.api.endpoint.BASE_URL
import me.thuongle.hknews.di.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideClient(@Interceptor interceptors: Array<okhttp3.Interceptor>): Retrofit {
        val preBuiltClient = OkHttpClient.Builder()

        // add logging as last interceptor
        for (interceptor in interceptors) {
            if ("StethoInterceptor" == interceptor.javaClass.simpleName) {
                preBuiltClient.networkInterceptors().add(interceptor)
            } else {
                preBuiltClient.interceptors().add(interceptor)
            }
        }

        preBuiltClient.connectTimeout(60, TimeUnit.SECONDS)
        preBuiltClient.readTimeout(60, TimeUnit.SECONDS)

        val client = preBuiltClient.build()

        //val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val gson = GsonBuilder().create()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}
