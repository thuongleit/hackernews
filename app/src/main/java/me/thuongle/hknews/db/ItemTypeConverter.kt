package me.thuongle.hknews.db

import android.arch.persistence.room.TypeConverter
import me.thuongle.hknews.vo.ItemType

object ItemTypeConverter {

    @TypeConverter
    @JvmStatic
    fun itemTypeToInt(type: ItemType?): Int? {
        return type?.ordinal
    }

    @TypeConverter
    @JvmStatic
    fun intToItemType(anInt: Int?): ItemType? {
        if (anInt == null) {
            return null
        }

        return ItemType.values()[anInt]
    }
}