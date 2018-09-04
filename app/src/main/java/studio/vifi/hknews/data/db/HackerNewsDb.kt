package studio.vifi.hknews.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import studio.vifi.hknews.data.vo.Item

@Database(
        entities = [
            Item::class],
        version = 1,
        exportSchema = false
)
abstract class HackerNewsDb : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}
