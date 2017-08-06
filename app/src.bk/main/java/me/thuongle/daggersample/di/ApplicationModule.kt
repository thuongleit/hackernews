package me.thuongle.daggersample.di

import android.app.Application
import android.content.Context

import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application  = application

    @Provides
    @ApplicationScope
    fun provideContext(): Context = application
}
