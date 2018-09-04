package studio.vifi.hknews.util

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun bundleOf(vararg pairs: Pair<String, Any>): Bundle {
    return Bundle().apply {
        pairs.forEach { pair ->
            val key = pair.first
            when (pair.second) {
                is String -> putString(key, pair.second as String)
                is Serializable -> putSerializable(key, pair.second as Serializable)
                is Parcelable -> putParcelable(key, pair.second as Parcelable)
                is Int -> putInt(key, pair.second as Int)
                is Long -> putLong(key, pair.second as Long)
                is Float -> putFloat(key, pair.second as Float)
                is Double -> putDouble(key, pair.second as Double)
            }
        }
    }
}