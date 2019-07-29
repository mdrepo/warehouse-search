package nz.co.warehouseandroidtest.features.productdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.features.entity.DataResource
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import org.junit.Rule

class ProductDetailUseCaseTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    val product = mock<ProductDetailResponse> { }
    private val productLiveData = MutableLiveData<ProductDetailResponse>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()

    private val resource: DataResource<ProductDetailResponse> = mock {
        on { data } doReturn productLiveData
        on { networkState } doReturn networkStateLiveData
    }
}