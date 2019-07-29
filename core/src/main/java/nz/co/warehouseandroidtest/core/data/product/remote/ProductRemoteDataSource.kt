package nz.co.warehouseandroidtest.core.data.product.remote

import nz.co.warehouseandroidtest.core.data.product.ProductDataSource
import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.core.entity.ProductDetailParam
import nz.co.warehouseandroidtest.core.mapper.PaginatedProductMapper
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.network.api.WarehouseService
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import java.io.IOException
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val api: WarehouseService,
    private val mapper: PaginatedProductMapper
) : ProductDataSource {
    override suspend fun getProductDetail(param: ProductDetailParam?): Result<ProductDetailResponse> {
        return try {
            val response = api.getProductDetail(param?.toMap() ?: emptyMap()).await()
            val responseBody = response.body()
            if (response.isSuccessful() && responseBody != null) {
                Result.success(responseBody)
            } else {
                Result.failed(RuntimeException("${response.code()}: ${response.message()}"))
            }
        } catch (e: IOException) {
            Result.failed(e)
        }
    }

    override suspend fun getProducts(
        start: Int,
        limit: Int,
        param: SearchCriteriaParam?
    ): Result<Paginated<Product>> {
        return try {
            val response = api.getSearchResult(
                startAt = start,
                limit = limit,
                paramMap = param?.toMap() ?: emptyMap()
            ).await()
            val responseBody = response.body()
            if (response.isSuccessful() && responseBody != null) {
                Result.success(mapper.map(responseBody))
            } else {
                Result.failed(RuntimeException("${response.code()}: ${response.message()}"))
            }
        } catch (e: IOException) {
            Result.failed(e)
        }
    }
}