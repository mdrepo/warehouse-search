package nz.co.warehouseandroidtest.features.searchproducts

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.network.models.Product

internal class SearchProductPagingDataSourceFactory(
    private val searchCriteria: SearchCriteriaParam?,
    private val productRepository: ProductRepository,
    private val coroutineContexts: CoroutineContexts
) : DataSource.Factory<Int, Product>() {

    internal val sourceLiveData = MutableLiveData<SearchProductsPagingDataSource>()

    override fun create(): DataSource<Int, Product> {
        val source = SearchProductsPagingDataSource(searchCriteria, productRepository, coroutineContexts)
        sourceLiveData.postValue(source)
        return source
    }
}