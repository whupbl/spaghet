package com.oi.spaghet1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpaghetAPI {
    @GET("/spaghet/cat")
    Call<CategoriesList> getCategories();

    @GET("/spaghet/find")
    Call<DishesList> findDishes(@Query("Sub") String sub,
                                @Query("PriceFrom") String from,
                                @Query("PriceTo") String to,
                                @Query("CookingTime") String time,
                                @Query("Str") String str);

    @GET("/spaghet/history/set/client")
    Call<RequestStatus> makeOrder(@Query("DishID") String dish, @Query("UserID") String user);

    @GET("/spaghet/history/assess/client")
    Call<RequestStatus> assessOrder(@Query("ID") String orderID, @Query("Mark") String mark);

    @GET("/spaghet/history/get/client")
    Call<ClientHistory> getHistory (@Query("ID") String userID);

}

