package studio.vifi.hknews.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagingRequestHelper
import studio.vifi.hknews.data.api.NetworkState

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

fun PagingRequestHelper.createStatusLiveData(): LiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(NetworkState.loading())
            report.hasError() -> liveData.postValue(
                    NetworkState.failed(getErrorMessage(report)))
            else -> liveData.postValue(NetworkState.loaded())
        }
    }
    return liveData
}
