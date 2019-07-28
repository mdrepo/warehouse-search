package nz.co.warehouseandroidtest.network.api

import kotlinx.coroutines.Deferred
import nz.co.warehouseandroidtest.network.Response
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import nz.co.warehouseandroidtest.network.models.SearchResultResponse
import nz.co.warehouseandroidtest.network.models.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WarehouseService {

    @GET("bolt/newuser.json")
    fun getNewUserId(): Deferred<Response<UserResponse>>

    @GET("bolt/price.json")
    fun getProductDetail(@QueryMap paramMap: Map<String, String>): Deferred<Response<ProductDetailResponse>>

    @GET("bolt/search.json")
    fun getSearchResult(
        @Query("Start") startAt: Int,
        @Query("Limit") limit: Int,
        @QueryMap paramMap: Map<String, String>
    ): Deferred<Response<SearchResultResponse>>
}