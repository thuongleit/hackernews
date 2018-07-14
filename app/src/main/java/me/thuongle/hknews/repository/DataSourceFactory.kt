package me.thuongle.hknews.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource

abstract class DataSourceFactory<Key, Value, DataSourceType : DataSource<Key, Value>> : DataSource.Factory<Key, Value>() {

    val liveSource: MutableLiveData<DataSourceType> = MutableLiveData()

    override fun create(): DataSource<Key, Value> {
        return createSource().also {
            liveSource.postValue(it)
        }
    }

    protected abstract fun createSource(): DataSourceType
}