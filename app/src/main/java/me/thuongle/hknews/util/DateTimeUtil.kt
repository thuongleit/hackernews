package me.thuongle.hknews.util

import android.text.format.DateUtils.*

fun getTimeAgo(timeSecond: Long): String {
    val timeMillis= timeSecond * 1000
    val now = System.currentTimeMillis()
    if (timeMillis > now || timeMillis <= 0) {
        return ""
    }

    val diff = now - timeMillis
    return when {
        diff < MINUTE_IN_MILLIS -> "${diff/ 1000}s"
        diff < 2 * MINUTE_IN_MILLIS -> "1m"
        diff < 50 * MINUTE_IN_MILLIS -> "${diff / MINUTE_IN_MILLIS}m"
        diff < 90 * MINUTE_IN_MILLIS -> "1hr"
        diff < 24 * HOUR_IN_MILLIS -> "${diff / HOUR_IN_MILLIS}hrs"
        diff < 48 * HOUR_IN_MILLIS -> "1d"
        else -> "${diff / DAY_IN_MILLIS}d"
    }
}