package nz.co.warehouseandroidtest.core.data.remote

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import nz.co.warehouseandroidtest.core.data.product.remote.ProductRemoteDataSource
import nz.co.warehouseandroidtest.core.entity.Paginated
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.core.mapper.PaginatedProductMapper
import nz.co.warehouseandroidtest.network.Response
import nz.co.warehouseandroidtest.network.api.WarehouseService
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.network.models.SearchResultResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException

class ProductRemoteRepositoryTest {

    private val products: List<Product> = listOf(mockProduct("1"), mockProduct("100"))
    private val response: Response<SearchResultResponse> = mock {
        on { body() } doReturn mock { }
        on { errorBody() } doReturn "Error from Response"
    }
    private val apiResult = CompletableDeferred<Response<SearchResultResponse>>()

    private val api: WarehouseService = mock {
        on { getSearchResult(any(), any(), anyOrNull()) } doReturn apiResult
    }
    private val mapper: PaginatedProductMapper = mock {
        on { map(any()) } doReturn Paginated(2, products)
    }
    private val dataSource = ProductRemoteDataSource(api, mapper)

    @Test
    fun `getProducts with successful response (non-null SearchCriteriaParam)`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(true)
            apiResult.complete(response)

            val queryMap = mapOf("key" to "value")
            val param: SearchCriteriaParam = mock {
                on { toMap() } doReturn queryMap
            }
            val result = dataSource.getProducts(1, 20, param)
            assertThat(result).isInstanceOf(Result.Success::class.java)
            val data = (result as Result.Success).data
            assertThat(data.totalItems).isEqualTo(2)
            assertThat(data.items).hasSize(2)
        }
    }

    @Test
    fun `getProducts with successful response (null SearchCriteriaParam)`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(true)
            apiResult.complete(response)

            val result = dataSource.getProducts(1, 20, null)
            Assertions.assertThat(result).isInstanceOf(Result.Success::class.java)
            val data = (result as Result.Success).data
            Assertions.assertThat(data.totalItems).isEqualTo(2)
            Assertions.assertThat(data.items).hasSize(2)
            verify(api).getSearchResult(eq(1), eq(20), eq(emptyMap()))
        }
    }

    @Test
    fun `getProducts with unsuccessful response`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(false)
            apiResult.complete(response)

            val result = dataSource.getProducts(1, 20, mock { })
            assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }

    @Test
    fun `getProducts with successful response and empty body`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(true)
            whenever(response.body()).thenReturn(null)
            apiResult.complete(response)

            val result = dataSource.getProducts(1, 20, mock { })
            assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }

    @Test
    fun `getProducts with IOException`() {
        runBlocking {
            apiResult.completeExceptionally(IOException("Error from Exception"))
            val result = dataSource.getProducts(1, 20, mock { })
            assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }
    private fun mockProduct(productKey: String): Product {
        return Product(
            class0 = null,
            description = null,
            barcode = null,
            branchPrice = null,
            class0Id = null,
            classId = null,
            dept = null,
            deptId = null,
            imageURL = null,
            itemCode = null,
            itemDescription = null,
            klass = null,
            price = null,
            productKey = productKey,
            subclass = null,
            subClassId = null,
            subDept = null,
            subDeptId = null
        )
    }
}