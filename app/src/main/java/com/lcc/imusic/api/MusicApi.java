package com.lcc.imusic.api;

import com.google.gson.JsonObject;
import com.lcc.imusic.bean.LoginBean;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.MusiciansBean;
import com.lcc.imusic.bean.SongsBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

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

    @Multipart
    @POST("auth/avatar")
    void upload(@Part("photo") MultipartBody photo);

    @FormUrlEncoded
    @DELETE("auth/logout")
    String logout(@Field("id") String id);

    @GET("auth/me")
    Call<JsonObject> me();

    @GET("song")
    Call<Msg<SongsBean>> songs(@Query("pageNum") int pageNum);

    @GET("song")
    Call<Msg<SongsBean>> songs(@Query("musicianid") long id, @Query("pageNum") int pageNum);

    @GET("musician")
    Call<Msg<MusiciansBean>> musicians(@Query("pageNum") int pageNum);

    @GET("musician")
    Call<Msg<MusiciansBean.MusicianItem>> musicians(@Query("id") long id);
}
