package me.thuongle.daggersample.api.endpoint

import io.reactivex.Flowable
import me.thuongle.daggersample.api.model.Item
import retrofit2.http.GET
import retrofit2.http.Path

val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

interface Api {

    @GET("topstories.json")
    fun getTopStories(): Flowable<ArrayList<Long>>

    @GET("newstories.json")
    fun getNewStories(): Flowable<ArrayList<Long>>

    @GET("beststories.json")
    fun getBestStories(): Flowable<ArrayList<Long>>

    @GET("askstories.json")
    fun getAskstories(): Flowable<ArrayList<Long>>

    @GET("showstories.json")
    fun getShowstories(): Flowable<ArrayList<Long>>

    @GET("jobstories.json")
    fun getJobstories(): Flowable<ArrayList<Long>>

    @GET("item/{id}.json")
    fun getItemDetailWith(@Path("id") id: String): Flowable<Item>

}

