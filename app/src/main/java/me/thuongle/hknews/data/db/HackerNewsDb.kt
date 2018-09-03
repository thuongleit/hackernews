package me.thuongle.hknews.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import me.thuongle.hknews.data.vo.Item

@Database(
        entities = [
            Item::class],
        version = 1,
        exportSchema = false
)
abstract class HackerNewsDb : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}
