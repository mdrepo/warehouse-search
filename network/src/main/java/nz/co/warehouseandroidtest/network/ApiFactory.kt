package nz.co.warehouseandroidtest.network

import com.google.gson.Gson
import nz.co.warehouseandroidtest.network.adapter.CoroutineCallAdapterFactory
import nz.co.warehouseandroidtest.network.interceptor.DefaultHeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiFactory constructor(baseUrl: String, subscriptionKey: String, gson: Gson) {

    private val httpClient: OkHttpClient
    private val retrofit: Retrofit

    companion object {
        const val DEFAULT_TIMEOUT = 20L
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(DefaultHeaderInterceptor(subscriptionKey))
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()
    }

    fun <T> create(klass: Class<T>): T = retrofit.create(klass)
}
