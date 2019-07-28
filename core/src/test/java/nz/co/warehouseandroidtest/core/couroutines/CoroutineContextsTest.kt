package nz.co.warehouseandroidtest.core.couroutines

import kotlinx.coroutines.isActive
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CoroutineContextsTest {

    private val coroutineContexts = CoroutineContexts()

    @Test
    fun `cancel should cancel child jobs`() {
        assertThat(coroutineContexts.ui.isActive).isTrue()
        assertThat(coroutineContexts.background.isActive).isTrue()

        coroutineContexts.cancel()

        assertThat(coroutineContexts.ui.isActive).isFalse()
        assertThat(coroutineContexts.background.isActive).isFalse()
    }
}