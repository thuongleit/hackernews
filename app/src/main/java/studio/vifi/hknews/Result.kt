package studio.vifi.hknews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

@Suppress("unused")
sealed class Result<out R> {
    data class Pending<out T> (val data: T? = null): Result<T>()
    data class Running<out T> (val data: T? = null): Result<T>()
    data class Success<out T> (val data: T?)  : Result<T>()
    data class Failure<out T> (val exception: Exception, val data: T? = null) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Pending<*> -> "Pending[$data]"
            is Running<*> -> "Running[$data]"
            is Success<*> -> "Success[data=$data]"
            is Failure<*> -> "Failure[exception=$exception,data=$data]"
        }
    }

    enum class Status {
        PENDING,
        RUNNING,
        SUCCESS,
        FAILURE
    }
}

@Suppress("unused")
val Result<*>.succeeded get() = this is Result.Success && data != null

@Suppress("unused")
val Result<*>.running get() = this is Result.Running<*>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

fun <T, R> Result<T>.map(transformer: (T) -> (R)): Result<R> {
    return when (this) {
        is Result.Pending<*> -> {
            Result.Pending()
        }
        is Result.Running<*> -> {
            Result.Running()
        }
        is Result.Success<*> -> {
            Result.Success(transformer(this.data as T))
        }
        is Result.Failure<*> -> {
            Result.Failure(this.exception)
        }
    }
}

fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

/** Uses `Transformations.switchMap` on a LiveData */
fun <X, Y> LiveData<X>.switchMap(body: (X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this, body)
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T?) {
    if (this.value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNew(newValue: T?) {
    if (this.value != newValue) postValue(newValue)
}

