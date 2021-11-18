package com.example.sportsballistics.data.api;


import com.example.sportsballistics.data.remote.login.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(URLIdentifiers.LOGIN)
    @FormUrlEncoded
    Call<UserResponse> login(
            @Field("email") String apiKey,
            @Field("password") String userID);


}


