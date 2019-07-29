package nz.co.warehouseandroidtest.features

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts

object TestUtils {

    fun testCoroutineContexts(): CoroutineContexts = mock {
        on { background } doReturn Dispatchers.Unconfined
        on { ui } doReturn Dispatchers.Unconfined
    }
}