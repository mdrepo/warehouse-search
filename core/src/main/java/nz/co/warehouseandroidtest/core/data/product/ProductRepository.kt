package nz.co.warehouseandroidtest.core.data.product

import nz.co.warehouseandroidtest.core.entity.ProductDetailParam
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remoteDataSource: ProductDataSource
): ProductDataSource {
    override suspend fun getProductDetail(param: ProductDetailParam?): Result<ProductDetailResponse> =
        remoteDataSource.getProductDetail(param)

    override suspend fun getProducts(start: Int, limit: Int, param: SearchCriteriaParam?) =
        remoteDataSource.getProducts(start, limit, param)
}