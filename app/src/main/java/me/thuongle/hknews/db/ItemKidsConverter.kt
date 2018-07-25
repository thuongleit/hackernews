package me.thuongle.hknews.db

import android.arch.persistence.room.TypeConverter
import timber.log.Timber

object ItemKidsConverter {

    @TypeConverter
    @JvmStatic
    fun stringToLongList(data: String?): List<Long>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toLong()
                } catch (e: NumberFormatException) {
                    Timber.e(e, "Cannot convert $it to long")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun longListToString(longs: List<Long>?): String? {
        return longs?.joinToString(separator = ",")
    }
}