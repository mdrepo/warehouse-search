package nz.co.warehouseandroidtest.network.models

import com.google.gson.annotations.SerializedName

data class SearchResultResponse(
    @SerializedName("HitCount") val hitCount: Int?,
    @SerializedName("Found") val found: String?,
    @SerializedName("Results") val results: List<ProductResult>?
) {
    fun isFound(): Boolean = "Y".equals(found, ignoreCase = true)
}

data class ProductResult(
    @SerializedName("Description") val description : String? = null,
    @SerializedName("Products") val products: List<Product>? = null
)