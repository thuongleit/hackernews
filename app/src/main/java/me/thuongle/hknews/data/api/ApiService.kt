package me.thuongle.hknews.data.api

import me.thuongle.hknews.data.vo.Item
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

interface ApiService {

    @GET("{path}")
    fun getStories(@Path("path") path: String): Call<List<Long>>

    @GET("item/{id}.json")
    fun getItemDetail(@Path("id") id: Long): Call<Item>
}

fun Item.StoryType.path(): String {
    return "${name.toLowerCase()}stories.json"
}