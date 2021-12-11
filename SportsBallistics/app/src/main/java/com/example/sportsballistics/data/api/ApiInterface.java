package com.example.sportsballistics.data.api;


import static com.example.sportsballistics.data.api.URLIdentifiers.DELETE_USER;
import static com.example.sportsballistics.data.api.URLIdentifiers.SKILL_POST;

import com.example.sportsballistics.data.remote.AthleteResponse;
import com.example.sportsballistics.data.remote.DashboardModel;
import com.example.sportsballistics.data.remote.ViewClubResponse;
import com.example.sportsballistics.data.remote.club.ClubResponse;
import com.example.sportsballistics.data.remote.form_service.FormServiceModel;
import com.example.sportsballistics.data.remote.generic.GenericResponse;
import com.example.sportsballistics.data.remote.dashboard.DashboardResponse;
import com.example.sportsballistics.data.remote.login.UserResponse;
import com.example.sportsballistics.data.remote.service.ServiceResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST(URLIdentifiers.LOGIN)
    @FormUrlEncoded
    Call<UserResponse> login(@Field("email") String apiKey, @Field("password") String userID);

    @POST(URLIdentifiers.GET_ALL_CLUBS)
    @FormUrlEncoded
    Call<ClubResponse> getContent(@Field("select_limit") int limit, @Field("action") String action, @Field("search_text") String search_text);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST(URLIdentifiers.GET_ALL_CLUBS)
    @FormUrlEncoded
    Call<GenericResponse> getMainContent(@Field("select_limit") int limit, @Field("action") String action, @Field("search_text") String search_text);

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

    @POST(URLIdentifiers.ADD_USER)
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
    @FormUrlEncoded
    @POST(URLIdentifiers.ADD_USER)
    Call<DashboardModel> addTrainer(@Field("email") String email,
            @Field("fullname") String fullname,
            @Field("contact_no") String contact_no,
            @Field("age") String age,
            @Field("state") String state,
            @Field("zipcode") int zipcode,
            @Field("city") String city,
            @Field("status") String status,
            @Field("address") String address,
            @Field("grade") String grade,
            @Field("password") String password,
            @Field("package_type") String package_type,
            @Field("club_name") String club_name,
            @Field("role_id") String role_id);

    @Multipart
    @POST(URLIdentifiers.ADD_USER)
    Call<DashboardModel> addTrainer(@Query("email") String email,
            @Query("fullname") String fullname,
            @Query("contact_no") String contact_no,
            @Query("age") String age,
            @Query("state") String state,
            @Query("zipcode") int zipcode,
            @Query("city") String city,
            @Query("status") String status,
            @Query("address") String address,
            @Query("grade") String grade,
            @Query("password") String password,
            @Query("package_type") String package_type,
            @Query("club_name") String club_name,
            @Query("role_id") String role_id,
            @Part MultipartBody.Part filePart);

    @POST(URLIdentifiers.EDIT_USER)
    @FormUrlEncoded
    Call<DashboardModel> editTrainer(@Path(value = "userid", encoded = true) String id, @Field("email") String email,
            @Field("fullname") String fullname,
            @Field("contact_no") String contact_no,
            @Field("age") String age,
            @Field("state") String state,
            @Field("zipcode") int zipcode,
            @Field("city") String city,
            @Field("status") String status,
            @Field("address") String address,
            @Field("grade") String grade,
            @Field("password") String password,
            @Field("package_type") String package_type,
            @Field("club_name") String club_name,
            @Field("role_id") String role_id);

    @POST(URLIdentifiers.VIEW_USER)
    Call<AthleteResponse> viewTrainer(@Path(value = "userid", encoded = true) String id);

    @POST(DELETE_USER)
    @FormUrlEncoded
    Call<DashboardModel> deleteTrainer(@Path(value = "userid", encoded = true) String id);

    @GET(URLIdentifiers.SERVICE_LIST_CONTENT)
    Call<ServiceResponseModel> getServiceContent(@Path("USER_ID") String path);

    @GET(URLIdentifiers.SERVICE_SLUG_DETAILS)
    Call<FormServiceModel> getFormInfo(@Path("SLUG") String slug, @Path("USER_ID") String path);

    @FormUrlEncoded
    @POST(SKILL_POST)
    Call<DashboardModel> submitSkillForm(@Path("SLUG") String slug, @Path("USER_ID") String id,
            @FieldMap Map<String, String> params
    );
}


