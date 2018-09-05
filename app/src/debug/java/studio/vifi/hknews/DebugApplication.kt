package studio.vifi.hknews

import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary

class DebugApplication : App() {

    override fun onCreate() {
        super.onCreate()

        //install debug tools
        LeakCanary.install(this)

        Stetho.initializeWithDefaults(this)
    }
}
