package studio.vifi.hknews.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.model.vo.StoryType

const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"
const val BROWSER_ID_URL = "https://news.ycombinator.com/item?id=%d"
const val BROWSER_VOTE_ID_URL = "https://news.ycombinator.com/vote?id=%d&how=up"

interface ApiService {

    @GET("{path}")
    fun getStories(@Path("path") path: String): Call<List<Long>>

    @GET("item/{id}.json")
    fun getItem(@Path("id") id: Long): Call<Item>
}

fun StoryType.path(): String {
    return "${name.toLowerCase()}stories.json"
}