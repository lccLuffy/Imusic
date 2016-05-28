package com.lcc.imusic.api;

import com.google.gson.JsonObject;
import com.lcc.imusic.bean.Club;
import com.lcc.imusic.bean.CollectedSongs;
import com.lcc.imusic.bean.CommentBean;
import com.lcc.imusic.bean.LoginBean;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.MusicianItem;
import com.lcc.imusic.bean.MusiciansBean;
import com.lcc.imusic.bean.SongsBean;
import com.lcc.imusic.bean.TopicReply;

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
    Call<Msg<LoginBean>> me();

    @FormUrlEncoded
    @POST("auth/me")
    Call<Msg<LoginBean>> updateMe(
            @Field("safeque") String safeque,
            @Field("safeans") String safeans,
            @Field("realname") String realName,
            @Field("phone") String phoneNum,
            @Field("mail") String mail
    );


    @GET("song")
    Call<Msg<SongsBean>> songs(@Query("pageNum") int pageNum);

    @FormUrlEncoded
    @POST("song/comment")
    Call<Msg<JsonObject>> commentToSong(@Field("songid") long songId, @Field("content") String content);

    @GET("song")
    Call<Msg<SongsBean>> musicianSongs(@Query("musicianid") long musicianId, @Query("pageNum") int pageNum);

    @GET("song/comment")
    Call<Msg<CommentBean>> songComment(@Query("songid") long id, @Query("pageNum") int pageNum);

    @GET("collection/song")
    Call<Msg<CollectedSongs>> collectedSongs(@Query("pageNum") int pageNum);

    @FormUrlEncoded
    @POST("collection/song")
    Call<Msg<String>> collectSong(@Field("songid") long songId);

    @DELETE("collection/song")
    Call<Msg<String>> cancelCollectSong(@Query("songid") long songId);

    @GET("musician")
    Call<Msg<MusiciansBean>> musicians(@Query("pageNum") int pageNum);

    @GET("musician")
    Call<Msg<MusicianItem>> musician(@Query("id") long id);

    @GET("collection/musician")
    Call<Msg<MusiciansBean>> collectionMusicians(@Query("pageNum") int pageNum);

    @GET("club")
    Call<Msg<Club>> club(@Query("musicianid") long musicianId, @Query("pageNum") int pageNum);

    @GET("topic")
    Call<Msg<Club.TopicItem>> topic(@Query("id") long topicId);

    @FormUrlEncoded
    @POST("topic")
    Call<Msg<JsonObject>> publishTopic(@Field("musicianid") long musicianId,
                                       @Field("title") String title,
                                       @Field("content") String content
    );

    @FormUrlEncoded
    @DELETE("topic")
    Call<Msg<JsonObject>> deleteTopic(@Field("id") long topicId);

    @GET("topic/reply")
    Call<Msg<TopicReply>> topicReplies(@Query("topicid") long topicId,
                                       @Query("pageNum") int pageNum
    );
}