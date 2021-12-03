package com.example.sportsballistics.data.api;


import com.example.sportsballistics.data.remote.DashboardModel;
import com.example.sportsballistics.data.remote.club.ClubResponse;
import com.example.sportsballistics.data.remote.generic.GenericResponse;
import com.example.sportsballistics.data.remote.dashboard.DashboardResponse;
import com.example.sportsballistics.data.remote.login.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST(URLIdentifiers.LOGIN)
    @FormUrlEncoded
    Call<UserResponse> login(
            @Field("email") String apiKey,
            @Field("password") String userID);

    @POST(URLIdentifiers.GET_ALL_CLUBS)
    @FormUrlEncoded
    Call<ClubResponse> getContent(
            @Field("select_limit") int limit,
            @Field("action") String action);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST(URLIdentifiers.GET_ALL_CLUBS)
    @FormUrlEncoded
    Call<GenericResponse> getMainContent(
            @Field("select_limit") int limit,
            @Field("action") String action);

    @POST(URLIdentifiers.GET_DASHBOARD)
    Call<DashboardResponse> getDashboard();

    @POST(URLIdentifiers.GET_GENERIC_DASHBOARD)
    Call<DashboardModel> getGenericDashboard(@Path("id") String id);
}


