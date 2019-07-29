package nz.co.warehouseandroidtest.network

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import retrofit2.Response as RetrofitResponse

class ResponseTest {

    private val retrofitResponse: RetrofitResponse<String> = mock {}

    @Test
    fun code() {
        whenever(retrofitResponse.code()).thenReturn(200)
        assertThat(Response(retrofitResponse).code()).isEqualTo(200)
    }

    @Test
    fun message() {
        whenever(retrofitResponse.message()).thenReturn("Message")
        assertThat(Response(retrofitResponse).message()).isEqualTo("Message")
    }

    @Test
    fun isSuccessful() {
        whenever(retrofitResponse.isSuccessful).thenReturn(true)
        assertThat(Response(retrofitResponse).isSuccessful()).isEqualTo(true)
    }

    @Test
    fun body() {
        whenever(retrofitResponse.body()).thenReturn("Body")
        assertThat(Response(retrofitResponse).body()).isEqualTo("Body")
    }

    @Test
    fun errorBodyNull() {
        whenever(retrofitResponse.errorBody()).thenReturn(null)
        assertThat(Response(retrofitResponse).errorBody()).isNull()
    }

    @Test
    fun errorBodyNotNull() {
        whenever(retrofitResponse.errorBody()).thenReturn(
            "Error".toResponseBody("application/json".toMediaTypeOrNull()))
        assertThat(Response(retrofitResponse).errorBody()).isEqualTo("Error")
    }
}