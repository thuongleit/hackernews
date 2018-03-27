package me.thuongle.hknews.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Immutable model class for an Item.
 * An Item can be any kind of {@link Type} In order to compile with Room,
 * use @JvmOverloads to generate multiple constructors.
 */
data class Item(
        val id: Long,
        val deleted: Boolean = false,
        val type: Type,
        @SerializedName("by") val byUser: String,
        val time: Long,
        val text: String,
        val dead: Boolean = false,
        val parent: Long?,
        val kids: ArrayList<Long>?,
        val url: String?,
        val score: Int?,
        val title: String,
        val parts: ArrayList<Long>?,
        val descendants: Int?
)