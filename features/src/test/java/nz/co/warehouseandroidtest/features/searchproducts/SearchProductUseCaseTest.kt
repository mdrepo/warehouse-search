package nz.co.warehouseandroidtest.features.searchproducts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.*
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.features.LoggingObserver
import nz.co.warehouseandroidtest.features.TestUtils.testCoroutineContexts
import nz.co.warehouseandroidtest.network.models.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchProductUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val Product = listOf(mock<Product>(), mock())
    private val resultSuccess = Result.success(mock<Paginated<Product>> {
        on { totalItems } doReturn 2
        on { items } doReturn Product
    })
    private val resultError = Result.failed(Exception("Error from Result"))

    private val repository: ProductRepository = mock { }
    private val contexts: CoroutineContexts = testCoroutineContexts()
    private lateinit var useCase: SearchProductsUseCase

    @Before
    fun setUp() {
        useCase = SearchProductsUseCase(repository, contexts)
    }

    @Test
    fun `search products should emit correct paged list`() {
        stubRepositoryReturnSuccess()
        val resource = useCase.searchProducts(mock { }, 10)
        val observerPagedList = LoggingObserver<PagedList<Product>>()
        resource.pagedList.observeForever(observerPagedList)
        assertThat(observerPagedList.value?.size).isEqualTo(2)
    }

    @Test
    fun `search products should emit correct total items`() {
        stubRepositoryReturnSuccess()
        val resource = useCase.searchProducts(mock { }, 10)
        resource.pagedList.observeForever(LoggingObserver<PagedList<Product>>())

        val observerTotalItems = LoggingObserver<Int>()
        resource.totalItems.observeForever(observerTotalItems)
        assertThat(observerTotalItems.value).isEqualTo(2L)
    }

    @Test
    fun `search products should emit correct network state`() {
        stubRepositoryReturnSuccess()
        val resource = useCase.searchProducts(mock { }, 10)
        resource.pagedList.observeForever(LoggingObserver<PagedList<Product>>())

        val observerNetworkState = LoggingObserver<NetworkState>()
        resource.networkState.observeForever(observerNetworkState)
        assertThat(observerNetworkState.value).isEqualTo(NetworkState.LOADED)
    }

    @Test
    fun `search products should emit correct refresh`() {
        stubRepositoryReturnError()
        val resource = useCase.searchProducts(mock { }, 10)
        val observerPagedList = LoggingObserver<PagedList<Product>>()
        resource.pagedList.observeForever(observerPagedList)
        val pagedList1 = observerPagedList.value

        stubRepositoryReturnSuccess()
        resource.refresh.invoke()
        assertThat(pagedList1?.dataSource?.isInvalid).isTrue()
    }

    @Test
    fun `search products should emit correct retry`() {
        stubRepositoryReturnError()
        val resource = useCase.searchProducts(mock { }, 10)
        resource.pagedList.observeForever(LoggingObserver<PagedList<Product>>())

        stubRepositoryReturnSuccess()
        resource.retry.invoke()
        verifyBlocking(repository, times(2)) {
            getProducts(eq(10), any(), any())
        }
    }

    @Test
    fun unsubscribe() {
        useCase.unsubscribe()
        verify(contexts).cancel()
    }

    private fun stubRepositoryReturnSuccess() {
        repository.stub {
            onBlocking { getProducts(any(), any(), any()) } doReturn resultSuccess
        }
    }

    private fun stubRepositoryReturnError() {
        repository.stub {
            onBlocking { getProducts(any(), any(), any()) } doReturn resultError
        }
    }
}