package nz.co.warehouseandroidtest.core.entity

sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val exception: Throwable) : Result<Nothing>()

    companion object {

        fun <T : Any> success(data: T): Result<T> = Result.Success(data)

        fun failed(exception: Throwable): Result<Nothing> = Result.Error(exception)
    }
}