package studio.vifi.hknews.data.db.converter

import android.arch.persistence.room.TypeConverter
import timber.log.Timber

object KidsConverter {

    @TypeConverter
    @JvmStatic
    fun fromDatabase(from: String?): List<Long>? {
        return from?.let {
            it
                    .split(",")
                    .map {
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
    fun toDatabase(input: List<Long>?): String? {
        return input?.joinToString(separator = ",")
    }
}
