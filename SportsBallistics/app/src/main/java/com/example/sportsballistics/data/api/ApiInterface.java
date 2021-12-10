package com.example.sportsballistics.data.api;


import com.example.sportsballistics.data.remote.DashboardModel;
import com.example.sportsballistics.data.remote.ViewClubResponse;
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

public interface ApiInterface
{

    @POST(URLIdentifiers.LOGIN)
    @FormUrlEncoded
    Call<UserResponse> login(@Field("email") String apiKey, @Field("password") String userID);

    @POST(URLIdentifiers.GET_ALL_CLUBS)
    @FormUrlEncoded
    Call<ClubResponse> getContent(@Field("select_limit") int limit, @Field("action") String action);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST(URLIdentifiers.GET_ALL_CLUBS)
    @FormUrlEncoded
    Call<GenericResponse> getMainContent(@Field("select_limit") int limit, @Field("action") String action);

    @POST(URLIdentifiers.GET_DASHBOARD)
    Call<DashboardResponse> getDashboard();

    @POST(URLIdentifiers.GET_GENERIC_DASHBOARD)
    Call<DashboardModel> getGenericDashboard(@Path("id") String id);

    @POST(URLIdentifiers.ADD_USER)
    @FormUrlEncoded
    Call<DashboardModel> addUser(@Field("email") int email, @Field("fullname") int fullname, @Field("contact_no") int contact_no, @Field("age") int age, @Field("state") int state, @Field("zipcode") int zipcode, @Field("city") int limit, @Field("status") int status, @Field("address") int address, @Field("grade") int grade, @Field("password") int password, @Field("package_type") int package_type, @Field("club_name") int club_name, @Field("role_id") int role_id, @Field("image_name") int image_name);


    @POST("user/edit/{userid}/1")
    @FormUrlEncoded
    Call<DashboardModel> editUser(@Path(value = "userid", encoded = true) String id, @Field("email") int email, @Field("fullname") int fullname, @Field("contact_no") int contact_no, @Field("age") int age, @Field("state") int state, @Field("zipcode") int zipcode, @Field("city") int limit, @Field("status") int status, @Field("address") int address, @Field("grade") int grade, @Field("password") int password, @Field("package_type") int package_type, @Field("club_name") int club_name, @Field("role_id") int role_id, @Field("image_name") int image_name);


    @POST("user/edit/{userid}/1/1")
    @FormUrlEncoded
    Call<DashboardModel> viewUser(@Path(value = "userid", encoded = true) String id);

    @POST("user/delete/{userid}/1")
    @FormUrlEncoded
    Call<DashboardModel> deleteUser(@Path(value = "userid", encoded = true) String id);

    @POST(URLIdentifiers.ADD_CLUB)
    @FormUrlEncoded
    Call<DashboardModel> addClub(
            @Field("name") String name,
            @Field("address") String address,
            @Field("state") String state,
            @Field("city") String city,
            @Field("status") String status,
            @Field("zipcode") int zipcode);

    @POST(URLIdentifiers.EDIT_CLUB)
    @FormUrlEncoded
    Call<DashboardModel> editClub(@Path(value = "club_id", encoded = true) String id,
            @Field("name") String name,
            @Field("address") String address,
            @Field("state") String state,
            @Field("city") String city,
            @Field("status") String status,
            @Field("zipcode") int zipcode);

    @POST(URLIdentifiers.VIEW_CLUB)
    Call<ViewClubResponse> viewClub(@Path(value = "club_id", encoded = true) String id);

    @POST("club/delete/{club_id}/1")
    @FormUrlEncoded
    Call<DashboardModel> deleteClub(@Path(value = "club_id", encoded = true) String id);

    @POST(URLIdentifiers.ADD_CLUB)
    @FormUrlEncoded
    Call<DashboardModel> addTrainer(@Field("email") int email,
            @Field("fullname") int fullname,
            @Field("contact_no") int contact_no,
            @Field("age") int age,
            @Field("state") int state,
            @Field("zipcode") int zipcode,
            @Field("city") int limit,
            @Field("status") int status,
            @Field("address") int address,
            @Field("grade") int grade,
            @Field("password") int password,
            @Field("package_type") int package_type,
            @Field("club_name") int club_name,
            @Field("role_id") int role_id); //role will be for only role 4

    @POST(URLIdentifiers.EDIT_CLUB)
    @FormUrlEncoded
    Call<DashboardModel> editTrainer(@Path(value = "club_id", encoded = true) String id, @Field("name") int name, @Field("address") int address, @Field("state") int state, @Field("zipcode") int zipcode, @Field("city") int limit);

    @POST(URLIdentifiers.VIEW_CLUB)
    @FormUrlEncoded
    Call<DashboardModel> viewTrainer(@Path(value = "club_id", encoded = true) String id);

    @POST("club/delete/{trainer_id}/1")
    @FormUrlEncoded
    Call<DashboardModel> deleteTrainer(@Path(value = "club_id", encoded = true) String id);

}


