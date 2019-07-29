package nz.co.warehouseandroidtest.features.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.features.entity.DataResource
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(
    private val useCase: ProductDetailUseCase,
    private val coroutineContexts: CoroutineContexts
) : ViewModel() {
    private val _barcode = MutableLiveData<String>()
    private val dataResource: MutableLiveData<DataResource<ProductDetailResponse>> = MutableLiveData()
    val product: LiveData<ProductDetailResponse> = Transformations.switchMap(dataResource) {
        it.data
    }
    val networkState: LiveData<NetworkState> = Transformations.switchMap(dataResource) {
        it.networkState
    }

    fun setProductRequest(userId: String?, barcode: String?) {
        if (_barcode.value == barcode) {
            return
        }
        _barcode.value = barcode
        dataResource.value = useCase.dispatchFetchProduct(userId = userId, barCode = barcode)
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContexts.cancel()
    }
}