package nz.co.warehouseandroidtest.features.searchproducts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PageKeyedDataSource
import androidx.paging.PageKeyedDataSource.LoadCallback
import androidx.paging.PageKeyedDataSource.LoadInitialCallback
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.isNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyBlocking
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.core.entity.Status
import nz.co.warehouseandroidtest.features.LoggingObserver
import nz.co.warehouseandroidtest.features.TestUtils.testCoroutineContexts
import nz.co.warehouseandroidtest.network.models.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class SearchProductsPagingDataSourceTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val products = listOf(mock<Product>(), mock())
    private val resultSuccess = Result.success(mock<Paginated<Product>> {
        on { totalItems } doReturn 2
        on { items } doReturn products
    })
    private val resultError = Result.failed(Exception("Error from Result"))

    private val repository: ProductRepository = mock { }
    private val dataSource = SearchProductsPagingDataSource(mock { }, repository, testCoroutineContexts())

    @Test
    fun `load initial with successful response`() {
        stubRepositoryReturnSuccess()

        val callback = mock<LoadInitialCallback<Int, Product>>()
        arrangeAndActLoadInitial(callback)

        assertThat(getNetworkState(dataSource)).isEqualTo(NetworkState.LOADED)
        assertThat(getTotalItems(dataSource)).isEqualTo(2)
        verify(callback).onResult(check {
            assertThat(it).hasSize(2)
        }, isNull(), eq(2))
    }

    @Test
    fun `load after with successful response`() {
        stubRepositoryReturnSuccess()

        val callback = mock<LoadCallback<Int, Product>>()
        arrangeAndActLoadAfter(callback)

        assertThat(getNetworkState(dataSource)).isEqualTo(NetworkState.LOADED)
        verify(callback).onResult(check {
            assertThat(it).hasSize(2)
        }, eq(3))
    }

    @Test
    fun `load initial with unsuccessful response`() {
        stubRepositoryReturnError()

        val callback = mock<LoadInitialCallback<Int, Product>>()
        arrangeAndActLoadInitial(callback)

        val networkState = getNetworkState(dataSource)
        assertThat(networkState?.status).isEqualTo(Status.FAILED)
        assertThat(networkState?.msg).isEqualTo("Error from Result")
        assertThat(getTotalItems(dataSource)).isEqualTo(null)
        verify(callback, never()).onResult(any(), any(), any())
    }

    @Test
    fun `load after with unsuccessful response`() {
        stubRepositoryReturnError()

        val callback = mock<LoadCallback<Int, Product>>()
        arrangeAndActLoadAfter(callback)

        val networkState = getNetworkState(dataSource)
        assertThat(networkState?.status).isEqualTo(Status.FAILED)
        assertThat(networkState?.msg).isEqualTo("Error from Result")
        assertThat(getTotalItems(dataSource)).isEqualTo(null)
        verify(callback, never()).onResult(any(), any())
    }

    @Test
    fun `load initial then retry`() {
        stubRepositoryReturnError()

        val callback = mock<LoadInitialCallback<Int, Product>>()
        arrangeAndActLoadInitial(callback)

        dataSource.retry()
        verifyBlocking(repository, times(2)) {
            getProducts(eq(10), any(), any())
        }
    }

    @Test
    fun `load after then retry`() {
        stubRepositoryReturnError()

        val callback = mock<LoadCallback<Int, Product>>()
        arrangeAndActLoadAfter(callback)

        dataSource.retry()
        verifyBlocking(repository, times(2)) {
            getProducts(eq(20), any(), any())
        }
    }

    @Test
    fun `load before just do nothing`() {
        val callback = mock<LoadCallback<Int, Product>>()
        dataSource.loadBefore(mock { }, callback)
        verify(callback, never()).onResult(any(), any())
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

    private fun arrangeAndActLoadInitial(callback: LoadInitialCallback<Int, Product>) {
        val params = PageKeyedDataSource.LoadInitialParams<Int>(10, false)
        dataSource.loadInitial(params, callback)
    }

    private fun arrangeAndActLoadAfter(callback: LoadCallback<Int, Product>) {
        val params = PageKeyedDataSource.LoadParams(2, 10)
        dataSource.loadAfter(params, callback)
    }

    private fun getNetworkState(dataSource: SearchProductsPagingDataSource): NetworkState? {
        val observer = LoggingObserver<NetworkState>()
        dataSource.getNetworkState().observeForever(observer)
        return observer.value
    }

    private fun getTotalItems(dataSource: SearchProductsPagingDataSource): Int? {
        val observer = LoggingObserver<Int>()
        dataSource.getTotalItems().observeForever(observer)
        return observer.value
    }
}