package me.thuongle.hknews

import android.app.Application
import me.thuongle.hknews.di.ApplicationComponent
import me.thuongle.hknews.di.ApplicationModule
import me.thuongle.hknews.di.DaggerApplicationComponent

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
