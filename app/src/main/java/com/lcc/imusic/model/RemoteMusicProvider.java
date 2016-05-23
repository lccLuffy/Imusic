package com.lcc.imusic.model;

import android.support.annotation.NonNull;

import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.bean.SongsBean;
import com.lcc.imusic.manager.NetManager_;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class RemoteMusicProvider extends SimpleMusicProviderImpl {


    private long id;

    public RemoteMusicProvider(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void provideMusics(final OnProvideMusics onProvideMusics) {

        NetManager_.API().songs(1).enqueue(new Callback<Msg<SongsBean>>() {
            @Override
            public void onResponse(Call<Msg<SongsBean>> call, Response<Msg<SongsBean>> response) {
                SongsBean songsBean = response.body().Result;
                if (songsBean != null) {
                    onProvideMusics.onSuccess(songsBean);
                }

            }

            @Override
            public void onFailure(Call<Msg<SongsBean>> call, Throwable t) {
                if (onProvideMusics != null) {
                    onProvideMusics.onFail(t);
                }
            }
        });


    }


    public static List<MusicItem> m2l(@NonNull SongsBean songsBean) {
        List<MusicItem> musicList = new ArrayList<>();
        for (SongsBean.SongItem tracksBean : songsBean.list) {
            MusicItem musicItem = new MusicItem();
            musicItem.title = tracksBean.songname;
            musicItem.duration = 0;
            musicItem.data = NetManager_.DOMAIN + tracksBean.songpath;
            musicItem.artist = tracksBean.musicianName;
            musicItem.cover = NetManager_.DOMAIN + tracksBean.cover;
            musicItem.id = tracksBean.id;
            musicList.add(musicItem);
        }
        return musicList;
    }
}
