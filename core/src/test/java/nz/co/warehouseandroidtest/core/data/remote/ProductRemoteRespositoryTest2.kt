package nz.co.warehouseandroidtest.core.data.remote

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import nz.co.warehouseandroidtest.core.data.product.remote.ProductRemoteDataSource
import nz.co.warehouseandroidtest.core.entity.ProductDetailParam
import nz.co.warehouseandroidtest.core.entity.Result
import nz.co.warehouseandroidtest.network.Response
import nz.co.warehouseandroidtest.network.api.WarehouseService
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.IOException

class ProductRemoteRespositoryTest2 {

    private val product = mockProduct("123")
    private val response: Response<ProductDetailResponse> = mock {
        on { body() } doReturn mock { }
        on { errorBody() } doReturn "Error from Response"
    }
    private val apiResult = CompletableDeferred<Response<ProductDetailResponse>>()

    private val api: WarehouseService = mock {
        on { getProductDetail(anyOrNull()) } doReturn apiResult
    }

    @Test
    fun `getProducts with successful response`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(true)
            apiResult.complete(response)

            val queryMap = mapOf("key" to "value")
            val param: ProductDetailParam = mock {
                on { toMap() } doReturn queryMap
            }
            val result = dataSource.getProductDetail(mock {  })
            Assertions.assertThat(result).isInstanceOf(Result.Success::class.java)
            val data = (result as Result.Success).data
            Assertions.assertThat(data.product?.productKey).isEqualTo("123")
        }
    }

    @Test
    fun `getProducts with successful response (null ProductDetailParam)`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(true)
            apiResult.complete(response)

            val result = dataSource.getProductDetail(mock {  })
            Assertions.assertThat(result).isInstanceOf(Result.Success::class.java)
            val data = (result as Result.Success).data
            verify(api).getProductDetail(eq(emptyMap()))
        }
    }

    @Test
    fun `getProductDetail with unsuccessful response`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(false)
            apiResult.complete(response)

            val result = dataSource.getProductDetail(mock {  })
            Assertions.assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }

    @Test
    fun `getProductDetail with successful response and empty body`() {
        runBlocking {
            whenever(response.isSuccessful()).thenReturn(true)
            whenever(response.body()).thenReturn(null)
            apiResult.complete(response)

            val result = dataSource.getProductDetail(mock {  })
            Assertions.assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }

    @Test
    fun `getProductDetail with IOException`() {
        runBlocking {
            apiResult.completeExceptionally(IOException("Error from Exception"))
            val result = dataSource.getProductDetail(mock {  })
            Assertions.assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }

    private val dataSource = ProductRemoteDataSource(api, mock {  })

    private fun mockProduct(productKey: String): ProductDetailResponse {
        return ProductDetailResponse(
            machineID = null,
            action = null,
            branch = null,
            found = null,
            prodQAT = null,
            product = Product(
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
            ),
            scanBarcode = null,
            scanDateTime = null,
            scanID = null,
            userDescription = null,
            userID = null
        )
    }
}