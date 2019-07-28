package nz.co.warehouseandroidtest.network.interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class DefaultHeaderInterceptorTest {


    private val interceptor: DefaultHeaderInterceptor = DefaultHeaderInterceptor("subscription-key")

    private val server = MockWebServer()
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Test
    fun `subscription key should be set in the header`() {
        server.enqueue(MockResponse())
        client.newCall(Request.Builder().url(server.url("/")).build()).execute()
        val headers = server.takeRequest().headers
        assertThat(headers[DefaultHeaderInterceptor.SUBSCRIPTION_KEY]).isEqualTo("subscription-key")
    }
}