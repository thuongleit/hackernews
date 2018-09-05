package studio.vifi.hknews.data.repository


sealed class NetworkState {
    companion object {
        fun loaded() = LOADED
        fun loading() = LOADING
        fun failed(msg: String? = null) = ERROR(msg)
    }
}

object LOADED : NetworkState()
object LOADING : NetworkState()
data class ERROR(val msg: String? = null) : NetworkState()