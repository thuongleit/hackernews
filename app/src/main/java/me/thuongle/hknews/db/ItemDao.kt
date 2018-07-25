package me.thuongle.hknews.db

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import android.arch.persistence.room.*
import me.thuongle.hknews.vo.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg items: Item)

    @Update
    fun update(vararg items: Item)

    @Delete
    fun delete(vararg items: Item)

    @Query("SELECT * FROM stories")
    fun loadAll(): LiveData<PagedList<Item>>

    @Query("SELECT * FROM stories WHERE id = :id")
    fun loadById(id: Long): LiveData<Item>

    @Query("SELECT * FROM stories WHERE id IN (:ids)")
    fun loadByIds(vararg ids: Long): LiveData<PagedList<Item>>
}