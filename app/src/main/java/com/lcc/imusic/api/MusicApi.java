package com.lcc.imusic.api;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lcc_luffy on 2016/4/20.
 */
public interface MusicApi {

    @FormUrlEncoded
    @POST("auth/signup")
    String signUp(@Field("username") String username, @Field("password") String password);


    @FormUrlEncoded
    @POST("auth/login")
    String login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @DELETE("auth/logout")
    String logout(@Field("id") String id);
}
