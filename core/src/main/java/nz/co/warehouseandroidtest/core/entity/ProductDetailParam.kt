package nz.co.warehouseandroidtest.core.entity

import com.google.gson.annotations.SerializedName

data class ProductDetailParam(
    @SerializedName("BarCode") val barCode: String? = null,
    @SerializedName("MachineID") val branch: Int = 1234567890,
    @SerializedName("UserID") val userId: String? = null,
    @SerializedName("BranchID") val branchId: Int = 208
): Param