package com.example.zesiumapplication.retrofit.clientinterface;


import com.example.zesiumapplication.retrofit.beans.User;
import com.example.zesiumapplication.retrofit.beans.UserDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ZesiumClientInterface {

    @FormUrlEncoded
    @POST("restController/loggedInUserAndroid")
    Call<User> validateUser(@Field("email") String email,
                            @Field("password") String password);

    @POST("restController/addUserAndroid")
    Call<UserDetails> addUser(@Body UserDetails userDetails);

}
