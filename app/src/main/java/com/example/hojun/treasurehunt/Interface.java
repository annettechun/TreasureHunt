package com.example.hojun.treasurehunt;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface Interface {
    //This method is used for "POST"
    @FormUrlEncoded
    @POST("/")
    public void insertUser(@Field("name") String name, @Field("score") int score,
                           Callback<Response> serverResponseCallback);
}