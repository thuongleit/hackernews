package me.thuongle.daggersample.di

import android.app.Application
import android.content.Context
import me.thuongle.daggersample.api.endpoint.Api

import javax.inject.Singleton

import dagger.Component
import me.thuongle.daggersample.DebugModule
import me.thuongle.daggersample.api.NetworkModule

@Singleton
@Component(modules = arrayOf(DebugModule::class, NetworkModule::class, ApplicationModule::class))
interface ApplicationComponent {

    fun application(): Application

    @ApplicationScope
    fun context(): Context

    fun api(): Api
}
