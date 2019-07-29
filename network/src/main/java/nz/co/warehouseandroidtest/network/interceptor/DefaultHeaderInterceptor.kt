package nz.co.warehouseandroidtest.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class DefaultHeaderInterceptor(private val subscriptionKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(SUBSCRIPTION_KEY, subscriptionKey)
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key"
    }
}