package me.thuongle.hknews

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.work.Worker
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import me.thuongle.hknews.di.AppInjector
import me.thuongle.hknews.di.HasWorkerInjector
import timber.log.Timber
import javax.inject.Inject

open class App : Application(), HasActivityInjector, HasWorkerInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var workerInjector: DispatchingAndroidInjector<Worker>

    override fun onCreate() {
        super.onCreate()

        val crashlytics = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
        Fabric.with(this, Crashlytics.Builder().core(crashlytics).build())

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.plant(CrashlyticsTree())

        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

    override fun workerInjector(): AndroidInjector<Worker> = workerInjector

    inner class CrashlyticsTree : Timber.Tree() {
        private val KEY_PRIORITY = "priority"
        private val KEY_TAG = "tag"
        private val KEY_MESSAGE = "message"

        override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
            when (priority) {
                Log.VERBOSE, Log.DEBUG, Log.INFO -> return

                else -> {
                    Crashlytics.setInt(KEY_PRIORITY, priority)
                    Crashlytics.setString(KEY_TAG, tag)
                    Crashlytics.setString(KEY_MESSAGE, message)

                    if (t == null) {
                        Crashlytics.logException(Exception(message))
                    } else {
                        Crashlytics.logException(t)
                    }
                }
            }
        }
    }
}

