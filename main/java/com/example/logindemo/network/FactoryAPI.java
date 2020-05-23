package com.example.logindemo.network;

import com.example.logindemo.model.Announcements;
import com.example.logindemo.model.LoginData;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FactoryAPI {

   /* @POST("users")
    Observable<Response> register(@Body User user);
*/
    @POST("login")
    Call<ResponseBody> login(@Body LoginData body);

   @GET("getAll")
   Call<String> getAnnouncements();

    @GET("announcement/{id}")
    Call<ResponseBody> getAnnouncementsById(@Path("id") String id);

  /*  @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);*/
}
