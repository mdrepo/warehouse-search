package nz.co.warehouseandroidtest.network

import retrofit2.Response

class Response<T>(private val retrofitResponse: Response<T>) {

    fun code(): Int = retrofitResponse.code()

    fun message(): String = retrofitResponse.message()

    fun isSuccessful(): Boolean = retrofitResponse.isSuccessful

    fun body(): T? = retrofitResponse.body()

    fun errorBody(): String? = retrofitResponse.errorBody()?.string()
}