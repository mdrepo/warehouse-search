package nz.co.warehouseandroidtest.core.mapper

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.network.models.ProductResult
import nz.co.warehouseandroidtest.network.models.SearchResultResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PaginatedProductMapperTest {

    private val mapper = PaginatedProductMapper()
    private val product: Product = mock {
        on { barcode } doReturn "123-456"
        on { imageURL } doReturn "http://image.url"
    }
    private val productResult: ProductResult = mock {
        on { description } doReturn "description"
        on { products } doReturn listOf(product)
    }
    private val searchResultResponse: SearchResultResponse = mock {
        on { found } doReturn "Y"
        on { hitCount } doReturn 100
        on { results } doReturn listOf(productResult)
        on { isFound() } doReturn true
    }

    @Test
    fun `map empty json`() {
        val searchResultResponse: SearchResultResponse = mock {
            on { found } doReturn null
            on { hitCount } doReturn null
            on { results } doReturn null
        }
        val result = mapper.map(searchResultResponse)
        assertThat(result.totalItems).isEqualTo(0)
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `map basic properties`() {
        val result = mapper.map(searchResultResponse)
        assertThat(result.totalItems).isEqualTo(100)
        assertThat(result.items).isNotNull
        assertThat(result.items.size).isEqualTo(1)
        assertThat(result.items[0].barcode).isEqualTo("123-456")
        assertThat(result.items[0].imageURL).isEqualTo("http://image.url")
    }
}