package me.thuongle.daggersample

import android.app.Application
import me.thuongle.daggersample.di.ApplicationComponent
import me.thuongle.daggersample.di.ApplicationModule
import me.thuongle.daggersample.di.DaggerApplicationComponent

open class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
