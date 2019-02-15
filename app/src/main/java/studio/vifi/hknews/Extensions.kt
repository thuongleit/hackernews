package studio.vifi.hknews

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import studio.vifi.hknews.util.customtabs.CustomTabActivityHelper

fun Context.isConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnected == true
}

fun openUrlInCustomTab(activity: Activity, url: String) {
    CustomTabActivityHelper.openCustomTab(
            activity,
            CustomTabsIntent.Builder()
                    .setToolbarColor(
                            ContextCompat.getColor(
                                    activity,
                                    R.color.colorPrimary
                            )
                    )
                    .setShowTitle(true)
                    .enableUrlBarHiding()
                    .setSecondaryToolbarColor(ContextCompat.getColor(
                            activity,
                            android.R.color.white
                    ))
                    .addDefaultShareMenuItem()
                    .build(),
            url.toUri()
    )
}