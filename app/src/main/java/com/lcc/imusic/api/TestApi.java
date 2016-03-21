package com.lcc.imusic.api;

import com.lcc.imusic.bean.M163;
import com.lcc.imusic.bean.Song;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lcc_luffy on 2016/3/17.
 */
public interface TestApi {
    @GET("https://api.douban.com/v2/music/search?q=love")
    Call<Song> getSongs();

    @GET("http://music.163.com/api/playlist/detail")
    Call<M163> get(@Query("id") long id);
}
