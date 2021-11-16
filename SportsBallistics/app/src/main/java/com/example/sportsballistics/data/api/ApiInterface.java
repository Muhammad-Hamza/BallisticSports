package com.example.sportsballistics.data.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(URLIdentifiers.PLACE_ORDER)
    @FormUrlEncoded
    Call<String> placeOrder(
            @Field("api_key") String apiKey,
            @Field("user_id") String userID,
            @Field("city") String city,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("phone_number") String phoneNumber,
            @Field("date") String date,
            @Field("order_time") String time,
            @Field("transaction_id") String transaction,
            @Field("notes") String notes);


}


