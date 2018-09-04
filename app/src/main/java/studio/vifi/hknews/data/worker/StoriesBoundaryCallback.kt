package studio.vifi.hknews.data.worker

import android.arch.paging.PagedList
import android.arch.paging.PagingRequestHelper
import android.support.annotation.MainThread
import studio.vifi.hknews.data.api.ApiService
import studio.vifi.hknews.data.api.path
import studio.vifi.hknews.data.vo.Item
import studio.vifi.hknews.util.createStatusLiveData
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.Executor

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 * <p>
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class StoriesBoundaryCallback(
        private val requestType: Item.StoryType,
        private val webservice: ApiService,
        private val ioExecutor: Executor,
        private val networkPageSize: Int,
        private val handleResponse: (List<Item>) -> Unit)
    : PagedList.BoundaryCallback<Item>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            ioExecutor.execute {
                createWebserviceCallback(it, null, networkPageSize)
            }
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Item) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            ioExecutor.execute {
                createWebserviceCallback(it, itemAtEnd, networkPageSize)
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Item) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun createWebserviceCallback(callback: PagingRequestHelper.Request.Callback,
                                         after: Item?,
                                         networkPageSize: Int) {
        try {
            val itemsResponse = webservice
                    .getItems(requestType.path())
                    .execute()

            if (itemsResponse.isSuccessful) {
                itemsResponse.body()?.let { ids ->

                    var requestIndex = 0

                    if (after != null) {
                        if (ids.contains(after.id)) {
                            requestIndex = ids.indexOf(after.id) + 1
                        }
                    }
                    val items = ids
                            .subList(requestIndex, requestIndex + networkPageSize)
                            .mapNotNull { requestId ->
                                val itemDetailResponse = webservice.getItemDetail(requestId).execute()
                                return@mapNotNull if (itemDetailResponse.isSuccessful) {
                                    itemDetailResponse.body()
                                } else {
                                    Timber.e("Request failed for $requestId with ${itemDetailResponse.message()}")
                                    null
                                }
                            }.map {
                                it.copy(storyType = requestType)
                            }

                    handleResponse(items)
                    callback.recordSuccess()
                }!!
            } else {
                val message = itemsResponse.message()
                Timber.e(message)
                callback.recordFailure(IOException(message))
            }
        } catch (e: Exception) {
            Timber.e(e)
            callback.recordFailure(e)
        }
    }
}