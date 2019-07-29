package nz.co.warehouseandroidtest;

import nz.co.warehouseandroidtest.data.User;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WarehouseService {

    @GET("bolt/newuser.json")
    Call<User> getNewUserId();
}
