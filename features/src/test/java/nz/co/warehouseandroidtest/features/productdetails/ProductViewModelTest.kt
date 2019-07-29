package nz.co.warehouseandroidtest.features.productdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.features.LoggingObserver
import nz.co.warehouseandroidtest.features.entity.DataResource
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class ProductViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProductDetailViewModel
    val product = mock<ProductDetailResponse> { }
    private val productLiveData = MutableLiveData<ProductDetailResponse>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()

    private val resource: DataResource<ProductDetailResponse> = mock {
        on { data } doReturn productLiveData
        on { networkState } doReturn networkStateLiveData
    }
    private val useCase: ProductDetailUseCase = mock {
        on { dispatchFetchProduct(any(), any()) } doReturn resource
    }

    @Before
    fun setUp() {
        viewModel = ProductDetailViewModel(useCase)
    }

    @Test
    fun `onCleared should call unsubscribe`() {
        viewModel.onCleared()
        verify(useCase).unsubscribe()
    }

    @Test
    fun `fetch products should emit correct product detail`() {
        val productObserver = LoggingObserver<ProductDetailResponse>()
        viewModel.product.observeForever(productObserver)
        viewModel.setProductRequest(anyString(), anyString())

        productLiveData.postValue(product)

        Assertions.assertThat(productObserver.value).isEqualTo(product)
    }

    @Test
    fun `fetch listings should emit correct network state`() {
        val productsObserver = LoggingObserver<ProductDetailResponse>()
        viewModel.product.observeForever(productsObserver)
        viewModel.setProductRequest(anyString(), anyString())

        val networkStateObserver = LoggingObserver<NetworkState>()
        viewModel.networkState.observeForever(networkStateObserver)

        networkStateLiveData.postValue(NetworkState.LOADED)

        assertThat(networkStateObserver.value).isEqualTo(NetworkState.LOADED)
    }
}