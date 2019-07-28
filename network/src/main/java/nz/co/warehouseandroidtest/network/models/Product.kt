package nz.co.warehouseandroidtest.network.models

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("Class0") val class0: String?,
    @SerializedName("Price") val price: Price?,
    @SerializedName("Barcode") val barcode: String?,
    @SerializedName("ItemDescription") val itemDescription: String?,
    @SerializedName("DeptID") val deptId: String?,
    @SerializedName("SubClass") val subclass: String?,
    @SerializedName("Class0ID") val class0Id: String?,
    @SerializedName("SubDeptID") val subDeptId: String?,
    @SerializedName("Description") val description: String?,
    @SerializedName("BranchPrice") val branchPrice: String?,
    @SerializedName("ItemCode") val itemCode: String?,
    @SerializedName("SubDept") val subDept: String?,
    @SerializedName("ClassID") val classId: String?,
    @SerializedName("ImageURL") val imageURL: String?,
    @SerializedName("Dept") val dept: String?,
    @SerializedName("SubClassID") val subClassId: String?,
    @SerializedName("Class") val klass: String?,
    @SerializedName("ProductKey") val productKey: String?
)

data class Price(
    @SerializedName("Price") val price: String?,
    @SerializedName("Type") val type: String?
)