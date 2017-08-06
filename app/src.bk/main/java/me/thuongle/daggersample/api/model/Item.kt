package me.thuongle.daggersample.api.model

import com.google.gson.annotations.SerializedName

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