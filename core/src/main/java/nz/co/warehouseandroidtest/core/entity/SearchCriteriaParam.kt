package nz.co.warehouseandroidtest.core.entity

import com.google.gson.annotations.SerializedName

data class SearchCriteriaParam(
    @SerializedName("Search") val search: String? = null,
    @SerializedName("Branch") val branch: Int = 208,
    @SerializedName("UserID") val userId: String? = null
) : Param