package me.thuongle.hknews.api

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import me.thuongle.hknews.db.ItemKidsConverter
import me.thuongle.hknews.db.ItemTypeConverter
import me.thuongle.hknews.util.getBaseDomain
import java.util.*

enum class StoryType {
    NEW, TOP, BEST, ASK, SHOW, JOB
}

enum class ItemType {
    STORY, COMMENT, ASK, JOB, POLL, POLLOPT
}

@Entity(tableName = "stories",
        primaryKeys = ["id"],
        indices = [
            Index("id")]
)
@TypeConverters(ItemKidsConverter::class, ItemTypeConverter::class)
data class Item(
        val id: Long,
        val deleted: Boolean = false,
        val type: ItemType? = null,
        @SerializedName("by")
        @ColumnInfo(name = "by")
        val byUser: String,
        val time: Long,
        val text: String?,
        val dead: Boolean = false,
        val parent: Long?,
        val kids: ArrayList<Long>? = null,
        val url: String?,
        val score: Int = 0,
        val title: String,
        val parts: ArrayList<Long>? = null,
        val descendants: Int = 0
) : Parcelable {
    val description: String
        get() {
            val baseUrl = if (url.isNullOrBlank()) "" else "(${getBaseDomain(url)})"
            return "by $byUser $baseUrl"
        }

    val valid: Boolean
        get() = id != -1L

    constructor(source: Parcel) : this(
            source.readLong(),
            1 == source.readInt(),
            source.readValue(Int::class.java.classLoader)?.let { ItemType.values()[it as Int] },
            source.readString(),
            source.readLong(),
            source.readValue(String::class.java.classLoader) as String?,
            1 == source.readInt(),
            source.readValue(Long::class.java.classLoader) as Long?,
            ArrayList<Long>().apply { source.readList(this, Long::class.java.classLoader) },
            source.readValue(String::class.java.classLoader) as String?,
            source.readInt(),
            source.readString(),
            ArrayList<Long>().apply { source.readList(this, Long::class.java.classLoader) },
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeInt((if (deleted) 1 else 0))
        writeValue(type?.ordinal)
        writeString(byUser)
        writeLong(time)
        writeValue(text)
        writeInt((if (dead) 1 else 0))
        writeValue(parent) //null allowable
        writeList(kids)
        writeValue(url)
        writeInt(score)
        writeString(title)
        writeList(parts)
        writeInt(descendants)
    }

    companion object {
        fun createEmpty() =
                Item(
                        id = -1L,
                        byUser = "",
                        time = 0L,
                        text = "",
                        parent = 0,
                        url = null,
                        title = "")

        @JvmField
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item = Item(source)
            override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
        }
    }
}

