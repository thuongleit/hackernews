package me.thuongle.hknews.data.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName
import me.thuongle.hknews.data.db.converter.ItemTypeConverter
import me.thuongle.hknews.data.db.converter.KidsConverter
import me.thuongle.hknews.data.db.converter.StoryTypeConverter
import me.thuongle.hknews.util.getBaseDomain

@Entity(tableName = "stories",
        primaryKeys = ["id"],
        indices = [
            Index("id")]
)
@TypeConverters(KidsConverter::class, ItemTypeConverter::class, StoryTypeConverter::class)
data class Item(
        val id: Long,
        val deleted: Boolean = false,
        @ColumnInfo(name = "story_type")
        val storyType: StoryType,
        @SerializedName("type")
        @ColumnInfo(name = "item_type")
        val type: ItemType? = null,
        @SerializedName("by")
        @ColumnInfo(name = "by")
        val byUser: String,
        val time: Long,
        val text: String?,
        val dead: Boolean = false,
        val parent: Long?,
        val kids: List<Long>? = null,
        val url: String?,
        val score: Int = 0,
        val title: String,
        val parts: List<Long>? = null,
        val descendants: Int = 0
) {
    val description: String
        get() {
            val baseUrl = if (url.isNullOrBlank()) "" else "(${getBaseDomain(url)})"
            return "by $byUser $baseUrl"
        }

    val valid: Boolean
        get() = id != -1L

    enum class StoryType {
        NEW, TOP, BEST, ASK, SHOW, JOB
    }

    enum class ItemType {
        STORY, COMMENT, ASK, JOB, POLL, POLLOPT
    }
}


