package studio.vifi.hknews.data.db.converter

import android.arch.persistence.room.TypeConverter
import studio.vifi.hknews.data.vo.Item

object ItemTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toDatabase(input: Item.ItemType?): Int? {
        return input?.ordinal
    }

    @TypeConverter
    @JvmStatic
    fun fromDatabase(from: Int?): Item.ItemType? {
        if (from == null) {
            return null
        }

        return Item.ItemType.values()[from]
    }
}