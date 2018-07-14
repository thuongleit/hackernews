package me.thuongle.hknews.api

import com.google.gson.annotations.SerializedName

import me.thuongle.hknews.util.getBaseDomain
import java.util.*

enum class StoryType {
    NEW, TOP, BEST, ASK, SHOW, JOB
}

enum class ItemType {
    STORY, COMMENT, ASK, JOB, POLL, POLLOPT
}

data class Item(
        val id: Long,
        val deleted: Boolean = false,
        val type: ItemType? = null,
        @SerializedName("by") val byUser: String,
        val time: Long,
        val text: String,
        val dead: Boolean = false,
        val parent: Long?,
        val kids: ArrayList<Long>? = null,
        val url: String?,
        val score: Int = 0,
        val title: String,
        val parts: ArrayList<Long>? = null,
        val descendants: Int = 0
) {

    val description: String
        get() {
            val baseUrl = if (url.isNullOrBlank()) "" else "(${getBaseDomain(url)})"
            return "by $byUser $baseUrl"
        }

    val valid: Boolean
        get() = id != -1L

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
    }
}

