package studio.vifi.hknews.model.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import studio.vifi.hknews.AppExecutors
import studio.vifi.hknews.LiveResult
import studio.vifi.hknews.MutableLiveResult
import studio.vifi.hknews.Result
import studio.vifi.hknews.model.api.ApiService
import studio.vifi.hknews.model.api.path
import studio.vifi.hknews.model.vo.Item
import studio.vifi.hknews.model.vo.StoryType
import java.io.IOException
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(private val service: ApiService,
                                             private val executors: AppExecutors) : ItemRepository {

    override fun fetchStories(type: StoryType): LiveResult<List<Long>> {
        val result = MutableLiveResult<List<Long>>()
        executors
                .networkIO()
                .execute {
                    result.postValue(Result.Running())
                    service.getStories(type.path()).enqueue(object : Callback<List<Long>?> {
                        override fun onFailure(call: Call<List<Long>?>, t: Throwable) {
                            result.postValue(Result.Failure(Exception(t)))
                        }

                        override fun onResponse(call: Call<List<Long>?>, response: Response<List<Long>?>) {
                            if (response.isSuccessful) {
                                result.postValue(Result.Success(response.body()))
                            } else {
                                result.postValue(Result.Failure(IOException(response.errorBody().toString())))
                            }
                        }
                    })
                }

        return result
    }

    override fun fetchItem(itemId: Long): LiveResult<Item> {
        val result = MutableLiveResult<Item>()
        executors
                .networkIO()
                .execute {
                    result.postValue(Result.Running())
                    service.getItem(itemId).enqueue(object : Callback<Item> {
                        override fun onFailure(call: Call<Item?>, t: Throwable) {
                            result.postValue(Result.Failure(Exception(t)))

                        }

                        override fun onResponse(call: Call<Item?>, response: Response<Item?>) {
                            if (response.isSuccessful) {
                                result.postValue(Result.Success(response.body()))
                            } else {
                                result.postValue(Result.Failure(IOException(response.errorBody().toString())))
                            }
                        }
                    })
                }

        return result
    }
}