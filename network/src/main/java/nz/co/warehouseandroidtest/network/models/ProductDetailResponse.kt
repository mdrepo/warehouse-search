package nz.co.warehouseandroidtest.network.models

import com.google.gson.annotations.SerializedName

data class ProductDetailResponse(
    @SerializedName("MachineID") val machineID: String?,
    @SerializedName("Action") val action: String?,
    @SerializedName("ScanBarcode") val scanBarcode: String?,
    @SerializedName("ScanID") val scanID: String?,
    @SerializedName("UserDescription") val userDescription: String?,
    @SerializedName("Product") val product: Product?,
    @SerializedName("ProdQAT") val prodQAT: String?,
    @SerializedName("ScanDateTime") val scanDateTime: String?,
    @SerializedName("Found") val found: String?,
    @SerializedName("UserID") val userID: String?,
    @SerializedName("Branch") val branch: String?
) {
    fun isFound(): Boolean = "Y".equals(found, ignoreCase = true)
}



