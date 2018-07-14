package me.thuongle.hknews.repository


sealed class NetworkState {
    companion object {
        fun loaded() = LOADED()
        fun loading() = LOADING()
        fun failed(msg: String? = null) = FAILED(msg)
    }
}

class LOADED : NetworkState()
class LOADING : NetworkState()
data class FAILED(val msg: String? = null) : NetworkState()