package com.example.zesiumapplication.retrofit.clientinterface;


import com.example.zesiumapplication.retrofit.beans.User;
import com.example.zesiumapplication.retrofit.beans.UserDetails;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ZClientInterface {

    @FormUrlEncoded
    @POST("restController/loggedInUserAndroid")
    Call<User> validateUser(@Field("email") String email,
                            @Field("password") String password);

    @POST("restController/addUserAndroid")
    Call<UserDetails> addUser(@Body UserDetails userDetails);

    @PUT("restController/user/{id}")
    Call<ResponseBody> updateUser(@Body User user, @Path("id") Long id);

    @GET("restController/userByEmail/{email}")
    Call<List<User>> findByEmail(@Path("email") String email);

}
