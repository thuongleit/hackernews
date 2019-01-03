package studio.vifi.hknews.api

import io.reactivex.Single
import studio.vifi.hknews.vo.Item
import studio.vifi.hknews.vo.StoryType
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

interface ApiService {

    @GET("{path}")
    fun getStories(@Path("path") path: String): Single<List<Long>>

    @GET("item/{id}.json")
    fun getItemDetail(@Path("id") id: Long): Single<Item>
}

fun StoryType.path(): String {
    return "${name.toLowerCase()}stories.json"
}