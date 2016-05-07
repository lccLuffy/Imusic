package com.lcc.imusic.api;

import com.google.gson.JsonObject;
import com.lcc.imusic.bean.LoginBean;
import com.lcc.imusic.bean.Msg;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by lcc_luffy on 2016/4/20.
 */
public interface MusicApi {

    @FormUrlEncoded
    @POST("auth/signup")
    Call<JsonObject> signUp(@Field("username") String username,
                            @Field("password") String password,
                            @Field("safeque") String safeque,
                            @Field("safeans") String safeans
    );


    @FormUrlEncoded
    @POST("auth/login")
    Call<Msg<LoginBean>> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @DELETE("auth/logout")
    String logout(@Field("id") String id);

    @GET("auth/me")
    Call<JsonObject> me();
}
