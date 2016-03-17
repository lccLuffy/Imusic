package com.lcc.imusic.service;

import com.lcc.imusic.Bean.Song;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lcc_luffy on 2016/3/17.
 */
public interface TestApi {
    @GET("https://api.douban.com/v2/music/search?q=love")
    Call<Song> getSongs();
}
