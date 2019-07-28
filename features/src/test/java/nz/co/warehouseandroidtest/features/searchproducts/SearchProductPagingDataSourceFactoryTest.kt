package nz.co.warehouseandroidtest.features.searchproducts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import nz.co.warehouseandroidtest.features.LoggingObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchProductPagingDataSourceFactoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var factory: SearchProductPagingDataSourceFactory

    @Before
    fun setUp() {
        factory = SearchProductPagingDataSourceFactory(mock { }, mock { }, mock { })
    }

    @Test
    fun create() {
        val dataSourceFromFactory = factory.create()
        val observer = LoggingObserver<SearchProductsPagingDataSource>()
        factory.sourceLiveData.observeForever(observer)

        assertThat(dataSourceFromFactory).isNotNull
            .isEqualTo(observer.value)
    }
}