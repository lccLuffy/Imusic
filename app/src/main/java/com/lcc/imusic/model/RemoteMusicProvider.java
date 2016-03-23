package com.lcc.imusic.model;

import android.support.annotation.NonNull;

import com.lcc.imusic.api.TestApi;
import com.lcc.imusic.bean.M163;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.NetManager;

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

    @Override
    public void provideMusics(final OnProvideMusics onProvideMusics) {
        TestApi testApi = NetManager.create(TestApi.class);
        testApi.get(id).enqueue(new Callback<M163>() {
            @Override
            public void onResponse(Call<M163> call, Response<M163> response) {
                M163 m163 = response.body();

                if (onProvideMusics != null) {
                    onProvideMusics.onSuccess(m163);
                }
            }

            @Override
            public void onFailure(Call<M163> call, Throwable t) {
                if (onProvideMusics != null) {
                    onProvideMusics.onFail(t);
                }
            }
        });
    }

    public static List<MusicItem> m2l(@NonNull M163 m163) {
        List<MusicItem> musicList = new ArrayList<>();
        for (M163.ResultBean.TracksBean tracksBean : m163.result.tracks) {
            MusicItem musicItem = new MusicItem();
            musicItem.title = tracksBean.name;
            musicItem.duration = tracksBean.duration / 1000;
            musicItem.data = tracksBean.mp3Url;
            musicItem.artist = tracksBean.artists.get(0).name;
            musicItem.cover = tracksBean.album.picUrl;
            musicList.add(musicItem);
        }
        return musicList;
    }
}
