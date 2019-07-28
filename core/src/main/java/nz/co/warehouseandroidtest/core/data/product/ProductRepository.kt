package nz.co.warehouseandroidtest.core.data.product

import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remoteDataSource: ProductDataSource
): ProductDataSource {

    override suspend fun getProducts(start: Int, limit: Int, param: SearchCriteriaParam?) =
        remoteDataSource.getProducts(start, limit, param)
}