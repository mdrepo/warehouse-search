package nz.co.warehouseandroidtest.features.searchproducts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.*
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.features.LoggingObserver
import nz.co.warehouseandroidtest.features.entity.PagedResource
import nz.co.warehouseandroidtest.network.models.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchResultsListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchResultsListViewModel
    private val pagedListLiveData = MutableLiveData<PagedList<Product>>()
    private val totalItemsLiveData = MutableLiveData<Int>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()
    private val refreshFunc: () -> Unit = mock { }
    private val retryFunc: () -> Unit = mock { }

    private val resource: PagedResource<Product> = mock {
        on { pagedList } doReturn pagedListLiveData
        on { networkState } doReturn networkStateLiveData
        on { refresh } doReturn refreshFunc
        on { retry } doReturn retryFunc
        on { totalItems } doReturn totalItemsLiveData
    }
    private val cachedListingIds = MutableLiveData<List<Long>>()

    private val useCase: SearchProductsUseCase = mock {
        on { searchProducts(any(), any()) } doReturn resource
    }

    @Before
    fun setUp() {
        viewModel = SearchResultsListViewModel(useCase)
    }

    @Test
    fun `onCleared should call unsubscribe`() {
        viewModel.onCleared()
        verify(useCase).unsubscribe()
    }

    @Test
    fun `fetch listings should emit correct paged list`() {
        val pagedListObserver = LoggingObserver<PagedList<Product>>()
        viewModel.products.observeForever(pagedListObserver)
        viewModel.fetchProducts(mock { }, 10)

        val pagedList: PagedList<Product> = mock { }
        pagedListLiveData.postValue(pagedList)

        assertThat(pagedListObserver.value).isEqualTo(pagedList)
        assertThat(viewModel.hasFetched).isTrue()
    }

    @Test
    fun `fetch listings should emit correct total items`() {
        val pagedListObserver = LoggingObserver<PagedList<Product>>()
        viewModel.products.observeForever(pagedListObserver)
        viewModel.fetchProducts(mock { }, 10)

        val totalItemsObserver = LoggingObserver<Int>()
        viewModel.totalItems.observeForever(totalItemsObserver)

        totalItemsLiveData.postValue(10)

        assertThat(totalItemsObserver.value).isEqualTo(10)
    }

    @Test
    fun `fetch listings should emit correct network state`() {
        val pagedListObserver = LoggingObserver<PagedList<Product>>()
        viewModel.products.observeForever(pagedListObserver)
        viewModel.fetchProducts(mock { }, 10)

        val networkStateObserver = LoggingObserver<NetworkState>()
        viewModel.networkState.observeForever(networkStateObserver)

        networkStateLiveData.postValue(NetworkState.LOADED)

        assertThat(networkStateObserver.value).isEqualTo(NetworkState.LOADED)
    }

    @Test
    fun `retry after calling fetch listings should invoke retry function`() {
        viewModel.fetchProducts(mock { }, 10)
        viewModel.retry()
        verify(retryFunc).invoke()
    }

    @Test
    fun `retry without calling fetch listings first should do nothing`() {
        viewModel.retry()
        verify(retryFunc, never()).invoke()
    }

    @Test
    fun `refresh after calling get listings should invoke refresh function`() {
        viewModel.fetchProducts(mock { }, 10)
        viewModel.refresh()
        verify(refreshFunc).invoke()
    }

    @Test
    fun `refresh without calling get listings first should do nothing`() {
        viewModel.refresh()
        verify(retryFunc, never()).invoke()
    }
}