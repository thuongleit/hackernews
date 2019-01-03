/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The file has been modified
 * Modifications copyright (C) 2018 Thuong Le
 */

package studio.vifi.hknews.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import studio.vifi.hknews.AppExecutors
import timber.log.Timber
import java.io.IOException

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
</ResultType> */
abstract class NetworkBoundResource<ResultType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<ResultType>()

    init {
        result.value = null
        appExecutors.diskIO().execute {
            val dbSource = loadFromDatabase()
            appExecutors.mainThread().execute {
                result.addSource(dbSource) { data ->
                    result.removeSource(dbSource)
                    if (shouldFetch(data)) {
                        fetchFromNetwork(dbSource)
                    } else {
                        result.addSource(dbSource) { newData ->
                            newData?.let { setValue(it) }
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: ResultType) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            newData?.let { setValue(it) }
        }
        appExecutors.networkIO().execute {
            val response = try {
                createServiceCall().asLiveData()
            } catch (e: IOException) {
                Timber.e(TAG, e, e.message)
                null
            }
            response?.let {
                appExecutors.mainThread().execute {
                    result.addSource(response) { apiResponse ->
                        result.removeSource(response)
                        result.removeSource(dbSource)
                        appExecutors.diskIO().execute {
                            apiResponse?.let { saveResponseData(it) }
                            val loadFromDatabaseSource = loadFromDatabase()
                            appExecutors.mainThread().execute {
                                // we specially request a new live data,
                                // otherwise we will get immediately last cached value,
                                // which may not be updated with latest results received from network.
                                result.addSource(loadFromDatabaseSource) { newData ->
                                    newData?.let { setValue(it) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<ResultType>

    @WorkerThread
    protected abstract fun loadFromDatabase(): LiveData<ResultType>

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    @WorkerThread
    protected abstract fun createServiceCall(): ResultType?

    @WorkerThread
    protected abstract fun saveResponseData(data: ResultType)

    @MainThread
    protected open fun onFetchFailed() {
    }

    companion object {
        private const val TAG = "NetworkBoundResource"
    }
}

private fun <RequestType> RequestType.asLiveData(): LiveData<RequestType> {
    return object : LiveData<RequestType>() {
        init {
            postValue(this@asLiveData)
        }
    }
}
