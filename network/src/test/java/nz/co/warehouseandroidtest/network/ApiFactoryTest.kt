package nz.co.warehouseandroidtest.network

import com.nhaarman.mockitokotlin2.mock
import nz.co.warehouseandroidtest.network.api.WarehouseService
import org.junit.Assert.assertNotNull
import org.junit.Test

class ApiFactoryTest {

    private val apiFactory = ApiFactory("http://baseUrl.com", "subcription-key", mock {})

    @Test
    fun create() {
        assertNotNull(apiFactory.create(WarehouseService::class.java))
    }
}