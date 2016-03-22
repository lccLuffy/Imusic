package com.lcc.imusic.model;

import com.lcc.imusic.api.TestApi;
import com.lcc.imusic.bean.M163;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.utils.RetrofitUtil;

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
        TestApi testApi = RetrofitUtil.create(TestApi.class);
        testApi.get(id).enqueue(new Callback<M163>() {
            @Override
            public void onResponse(Call<M163> call, Response<M163> response) {
                M163 m163 = response.body();
                for (M163.ResultBean.TracksBean tracksBean : m163.result.tracks) {
                    MusicItem musicItem = new MusicItem();
                    musicItem.title = tracksBean.name;
                    musicItem.duration = tracksBean.duration / 1000;
                    musicItem.data = tracksBean.mp3Url;
                    musicItem.artist = tracksBean.artists.get(0).name;
                    musicItem.cover = tracksBean.album.picUrl;
                    musicList.add(musicItem);
                }
                if (onProvideMusics != null) {
                    onProvideMusics.onSuccess(musicList);
                }
            }

            @Override
            public void onFailure(Call<M163> call, Throwable t) {
                if (onProvideMusics != null) {
                    onProvideMusics.onFail(t.getMessage());
                }
            }
        });
    }
}
