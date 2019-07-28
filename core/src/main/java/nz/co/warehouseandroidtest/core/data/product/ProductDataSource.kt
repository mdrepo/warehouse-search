package nz.co.warehouseandroidtest.core.data.product

import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.network.models.Product

interface ProductDataSource {

    suspend fun getProducts(
        start: Int,
        limit: Int,
        param: SearchCriteriaParam?
    ): Result<Paginated<Product>>
}