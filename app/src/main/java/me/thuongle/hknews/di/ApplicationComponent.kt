package me.thuongle.hknews.di

import android.app.Application
import android.content.Context
import me.thuongle.hknews.api.endpoint.Api

import javax.inject.Singleton

import dagger.Component
import me.thuongle.hknews.DebugModule
import me.thuongle.hknews.api.NetworkModule

@Singleton
@Component(modules = arrayOf(DebugModule::class, NetworkModule::class, ApplicationModule::class))
interface ApplicationComponent {

    fun application(): Application

    @ApplicationScope
    fun context(): Context

    fun api(): Api
}
