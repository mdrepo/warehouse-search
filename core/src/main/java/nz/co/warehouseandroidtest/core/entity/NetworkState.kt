package nz.co.warehouseandroidtest.core.entity

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null,
    val exception: Throwable? = null
) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.LOADING)
        fun error(msg: String?, exception: Throwable?) = NetworkState(
            Status.FAILED,
            msg,
            exception)
    }

    fun isError(): Boolean = status == Status.FAILED
}

enum class Status {
    SUCCESS,
    FAILED,
    LOADING
}