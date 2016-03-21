package com.lcc.imusic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.api.TestApi;
import com.lcc.imusic.bean.M163;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class RemoteMusicProvider implements MusicProvider {
    List<MusicItem> musicList;
    private int playingMusicIndex;


    private static RemoteMusicProvider musicProvider;

    public static RemoteMusicProvider get() {
        if (musicProvider == null)
            musicProvider = new RemoteMusicProvider();
        return musicProvider;
    }

    Random random = new Random(System.currentTimeMillis());

    private RemoteMusicProvider() {
        musicList = new ArrayList<>();
    }

    public static void getData(final OnMusicList onMusicList) {
        if (onMusicList != null && get().musicList.size() >= 1) {
            onMusicList.onSuccess(get().musicList);
            return;
        }
        TestApi testApi = RetrofitUtil.create(TestApi.class);
        testApi.get().enqueue(new Callback<M163>() {
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
                    get().musicList.add(musicItem);
                }
                if (onMusicList != null) {
                    onMusicList.onSuccess(get().musicList);
                }
            }

            @Override
            public void onFailure(Call<M163> call, Throwable t) {
                if (onMusicList != null) {
                    onMusicList.onFail(t.getMessage());
                }
            }
        });
    }

    @NonNull
    @Override
    public List<MusicItem> provideMusics() {
        return musicList;
    }

    @Nullable
    @Override
    public MusicItem getPlayingMusic() {
        if (playingMusic != null)
            return playingMusic;
        if (musicList != null && !musicList.isEmpty()) {
            return musicList.get(0);
        }
        return null;
    }

    private MusicItem playingMusic;

    @Override
    public void setPlayingMusic(int index) {
        boolean change = playingMusicIndex != index;
        playingMusicIndex = index;
        playingMusic = musicList.get(index);
        if (change) {
            EventsManager.get().dispatchPlayingIndexChangeListener(index);
        }
    }

    @Override
    public int getPlayingMusicIndex() {
        return playingMusicIndex;
    }
}
