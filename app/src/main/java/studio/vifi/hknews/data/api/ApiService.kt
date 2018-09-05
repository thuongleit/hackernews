package studio.vifi.hknews.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import studio.vifi.hknews.data.model.Item

interface ApiService {

    @GET("{path}")
    fun getItems(@Path("path") path: String): Call<List<Long>>

    @GET("item/{id}.json")
    fun getItemDetail(@Path("id") id: Long): Call<Item>
}