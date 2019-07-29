package nz.co.warehouseandroidtest.core.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.di.module.CoreModule
import nz.co.warehouseandroidtest.network.di.module.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    CoreModule::class,
    NetworkModule::class
])
interface CoreComponent {

    fun getCoroutineContexts(): CoroutineContexts

    fun getApplication(): Application

    fun getProductRepository(): ProductRepository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): CoreComponent
    }
}