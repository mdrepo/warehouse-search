package nz.co.warehouseandroidtest.testutils

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import nz.co.warehouseandroidtest.network.models.Product

object FakeProduct {

    fun get(): Product = mock {
        on { imageURL } doReturn "http://image.url"
        on { description } doReturn "Product description"
    }
}