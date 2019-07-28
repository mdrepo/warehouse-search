package nz.co.warehouseandroidtest.core.data

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import nz.co.warehouseandroidtest.core.data.product.ProductDataSource
import nz.co.warehouseandroidtest.core.data.product.ProductRepository
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import org.junit.Test

class ProductRepositoryTest {

    private val remoteDataSource: ProductDataSource = mock { }
    private val repository = ProductRepository(remoteDataSource)

    @Test
    fun getListings() {
        runBlocking {
            val param: SearchCriteriaParam = mock { }
            repository.getProducts(1, 20, param)
            verify(remoteDataSource).getProducts(1, 20, param)
        }
    }
}