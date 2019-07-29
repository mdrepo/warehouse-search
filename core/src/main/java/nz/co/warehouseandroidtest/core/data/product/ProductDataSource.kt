package nz.co.warehouseandroidtest.core.data.product

import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.core.entity.ProductDetailParam
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse

interface ProductDataSource {

    suspend fun getProducts(
        start: Int,
        limit: Int,
        param: SearchCriteriaParam?
    ): Result<Paginated<Product>>

    suspend fun getProductDetail(
        param: ProductDetailParam?
    ): Result<ProductDetailResponse>
}