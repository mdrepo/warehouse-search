package nz.co.warehouseandroidtest.features.searchproducts

import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.features.entity.PagedResource
import nz.co.warehouseandroidtest.network.models.Product
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val coroutineContexts: CoroutineContexts
) {
    fun searchProducts(searchCriteria: SearchCriteriaParam?, limit: Int): PagedResource<Product> {
        val dataSourceFactory = SearchProductPagingDataSourceFactory(searchCriteria,
            productRepository,
            coroutineContexts)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(limit)
            .setPageSize(limit)
            .build()
        val livePagedList = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .build()
        return PagedResource(
            pagedList = livePagedList,
            totalItems = switchMap(dataSourceFactory.sourceLiveData) {
                it.getTotalItems()
            },
            networkState = switchMap(dataSourceFactory.sourceLiveData) {
                it.getNetworkState()
            },
            refresh = {
                dataSourceFactory.sourceLiveData.value?.invalidate()
            },
            retry = {
                dataSourceFactory.sourceLiveData.value?.retry()
            }
        )
    }

    fun unsubscribe() {
        coroutineContexts.cancel()
    }
}