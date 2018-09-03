package me.thuongle.hknews.data.db.converter

import android.arch.persistence.room.TypeConverter
import me.thuongle.hknews.data.vo.Item

object StoryTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toDatabase(input: Item.StoryType?): Int? {
        return input?.ordinal
    }

    @TypeConverter
    @JvmStatic
    fun fromDatabase(from: Int?): Item.StoryType? {
        if (from == null) {
            return null
        }

        return Item.StoryType.values()[from]
    }
}