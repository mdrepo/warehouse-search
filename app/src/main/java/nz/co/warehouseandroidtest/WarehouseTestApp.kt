package nz.co.warehouseandroidtest

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment

import com.google.gson.GsonBuilder
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import nz.co.warehouseandroidtest.core.di.components.DaggerCoreComponent
import nz.co.warehouseandroidtest.di.components.DaggerAppComponent

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WarehouseTestApp : Application(), HasSupportFragmentInjector, HasActivityInjector {

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingFragmentInjector

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    var warehouseService: WarehouseService? = null
        private set

    override fun onCreate() {
        super.onCreate()

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder().addHeader("Ocp-Apim-Subscription-Key", "").build()
                return chain.proceed(request)
            }
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.HTTP_URL_ENDPOINT)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
        warehouseService = retrofit.create(WarehouseService::class.java)

        ZXingLibrary.initDisplayOpinion(this)
        val coreComponent = DaggerCoreComponent.builder()
            .application(this)
            .build()
        DaggerAppComponent.builder()
            .coreComponent(coreComponent)
            .build()
            .inject(this)
    }
}
