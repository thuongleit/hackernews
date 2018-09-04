package studio.vifi.hknews.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import studio.vifi.hknews.data.vo.Item

const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

interface ApiService {

    @GET("{path}")
    fun getItems(@Path("path") path: String): Call<List<Long>>

    @GET("item/{id}.json")
    fun getItemDetail(@Path("id") id: Long): Call<Item>
}

fun Item.StoryType.path(): String {
    return "${name.toLowerCase()}stories.json"
}