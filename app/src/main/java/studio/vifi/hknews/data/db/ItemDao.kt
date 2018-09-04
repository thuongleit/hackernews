package studio.vifi.hknews.data.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*
import studio.vifi.hknews.data.vo.Item

@Dao
abstract class ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg items: Item)

    @Update
    abstract fun update(vararg items: Item)

    @Delete
    abstract fun delete(vararg items: Item)

    @Query("SELECT * FROM items ORDER BY time DESC")
    abstract fun loadAll(): DataSource.Factory<Int, Item>

    @Query("SELECT * FROM items WHERE id = :id")
    abstract fun loadById(id: Long): LiveData<Item>

    @Query("SELECT * FROM items WHERE id IN (:ids) ORDER BY time DESC")
    abstract fun loadByIds(vararg ids: Long): DataSource.Factory<Int, Item>

    @Query("SELECT * FROM items WHERE story_type = :typeOrdinal ORDER BY time DESC")
    abstract fun loadByType(typeOrdinal: Int): DataSource.Factory<Int, Item>

    @Query("SELECT EXISTS (SELECT id FROM items WHERE id = :itemId)")
    abstract fun exists(itemId: Long): Boolean

    open fun loadByType(type: Item.StoryType): DataSource.Factory<Int, Item> {
        return loadByType(type.ordinal)
    }
}