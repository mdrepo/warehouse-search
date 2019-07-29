package nz.co.warehouseandroidtest.features.productdetails

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.core.entity.ProductDetailParam
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.features.entity.DataResource
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import javax.inject.Inject

class ProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val coroutineContexts: CoroutineContexts
) {
    private val networkState = MutableLiveData<NetworkState>()
    private val product = MutableLiveData<ProductDetailResponse>()

    fun dispatchFetchProduct(userId: String?, barCode: String?): DataResource<ProductDetailResponse> {
        CoroutineScope(coroutineContexts.background).launch {
            val productDetailResponse = getProductDetail(ProductDetailParam(
                userId = userId,
                barCode = barCode
            ))
            if (productDetailResponse != null) {
                product.postValue(productDetailResponse)
            }
        }
        return DataResource<ProductDetailResponse>(
            networkState = networkState,
            data = product
        )
    }

    private suspend fun getProductDetail(productDetailParam: ProductDetailParam): ProductDetailResponse? {
        networkState.postValue(NetworkState.LOADING)
        val result = productRepository.getProductDetail(productDetailParam)
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

    fun unsubscribe() {
        coroutineContexts.cancel()
    }
}