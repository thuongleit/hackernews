package studio.vifi.hknews.data.repository

sealed class NetworkState(val requestType: RequestType,
                          val error: Throwable? = null)

class LOADED(type: RequestType) : NetworkState(type)
class LOADING(type: RequestType) : NetworkState(type)
class ERROR(type: RequestType, throwable: Throwable? = null) : NetworkState(type, throwable)

enum class RequestType {
    INITIAL_LOAD, LOAD_MORE, REFRESH
}