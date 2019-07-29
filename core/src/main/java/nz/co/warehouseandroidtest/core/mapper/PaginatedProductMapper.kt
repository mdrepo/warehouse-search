package nz.co.warehouseandroidtest.core.mapper

import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.network.models.SearchResultResponse
import javax.inject.Inject

class PaginatedProductMapper @Inject constructor() : Mapper<SearchResultResponse, Paginated<Product>> {

    override fun map(input: SearchResultResponse): Paginated<Product> {
        val totalItems = input.hitCount ?: 0
        val mappedItems: List<Product>? = if (input.isFound() && totalItems > 0) {
            input.results?.mapNotNull { productResult ->
                productResult.products?.first()
            }
        } else {
            null
        }
        return Paginated(totalItems, mappedItems
            ?: emptyList())
    }
}