package nz.co.warehouseandroidtest.features.searchproducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.network.models.Product

internal class SearchProductsPagingDataSource(
    private val searchCriteria: SearchCriteriaParam?,
    private val productRepository: ProductRepository,
    private val coroutineContexts: CoroutineContexts
) : PageKeyedDataSource<Int, Product>() {

    private val totalItems = MutableLiveData<Int>()
    private val networkState = MutableLiveData<NetworkState>()
    private var retryFunc: (() -> Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Product>
    ) {
        CoroutineScope(coroutineContexts.background).launch {
            val page = 1
            val result = request(page, params.requestedLoadSize)
            if (result != null) {
                totalItems.postValue(result.totalItems)
                callback.onResult(result.items, null, page + 1)
            } else {
                retryFunc = {
                    loadInitial(params, callback)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        CoroutineScope(coroutineContexts.background).launch {
            val result = request(params.key, params.requestedLoadSize)
            if (result != null) {
                callback.onResult(result.items, params.key + 1)
            } else {
                retryFunc = {
                    loadAfter(params, callback)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        // no-op
    }

    fun getTotalItems(): LiveData<Int> = totalItems

    fun getNetworkState(): LiveData<NetworkState> = networkState

    fun retry() {
        val tempRetryFunc = retryFunc
        retryFunc = null
        tempRetryFunc?.invoke()
    }

    private suspend fun request(page: Int, limit: Int): Paginated<Product>? {
        networkState.postValue(NetworkState.LOADING)

        val result = productRepository.getProducts(page*limit, limit, searchCriteria)
        return when (result) {
            is Result.Success -> {
                networkState.postValue(NetworkState.LOADED)
                result.data
            }
            is Result.Error -> {
                val exception = result.exception
                networkState.postValue(NetworkState.error(exception.message, exception))
                null
            }
        }
    }
}