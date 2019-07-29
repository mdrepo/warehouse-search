package nz.co.warehouseandroidtest.core.di.module

import dagger.Binds
import dagger.Module
import nz.co.warehouseandroidtest.core.data.product.ProductDataSource
import nz.co.warehouseandroidtest.core.data.product.remote.ProductRemoteDataSource

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindListingsRemoteDataSource(dataSource: ProductRemoteDataSource): ProductDataSource
}