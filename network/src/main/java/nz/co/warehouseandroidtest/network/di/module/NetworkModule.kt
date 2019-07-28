package nz.co.warehouseandroidtest.network.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import nz.co.warehouseandroidtest.network.ApiFactory
import nz.co.warehouseandroidtest.network.BuildConfig
import nz.co.warehouseandroidtest.network.api.WarehouseService
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = BuildConfig.SERVER_URL

    @Provides
    @Named("subsciptionKey")
    fun provideSubscriptionKey(): String = BuildConfig.SUBSCRIPTION_KEY

    @Provides
    @Singleton
    fun providesApiFactory(
        @Named("subsciptionKey") subscriptionKey: String,
        @Named("baseUrl") baseUrl: String
    ) = ApiFactory(baseUrl, subscriptionKey, Gson())

    @Provides
    fun providesWarehouseService(apiFactory: ApiFactory): WarehouseService =
        apiFactory.create(WarehouseService::class.java)
}