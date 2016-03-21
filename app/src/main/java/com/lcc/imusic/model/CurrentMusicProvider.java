package com.lcc.imusic.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.api.TestApi;
import com.lcc.imusic.bean.M163;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.utils.RetrofitUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class CurrentMusicProvider implements MusicProvider {
    List<MusicItem> musicList;
    private int playingMusicIndex;
    private static String[] projection = {
            Media.TITLE,
            Media.ARTIST,
            Media.DATA,
            Media.DURATION,
            Media.ALBUM,
    };

    private String url1 = "http://www.n63.com/zutu/n63/?N=X2hiJTI2MC4tJTI4LSUyRiU1RCU1QzElMkJZJTJBMCU1QjAlNUQlMkIlMkElMkMtJTVFJTI4JTI4JTI5JTJGJTJDWiUyQiU1QyUyQjElMkMwWSUyNyUyQzBZ&v=.jpg";
    private String url2 = "http://upload.jianshu.io/users/upload_avatars/1438934/e9fe359cbaf2.jpeg";

    private String url3 = "http://img.666ccc.com/SpecialPic3/pic2010/19642.jpg";
    private static MusicProvider musicProvider;

    public static MusicProvider getMusicProvider(@NonNull Context context) {
        if (musicProvider == null)
            musicProvider = new CurrentMusicProvider(context);
        return musicProvider;
    }

    private final String test_url = "http://m1.music.126.net/jt_bjt-DDWhFI9btE2b8tw==/7901090557280522.mp3";
    Random random = new Random(System.currentTimeMillis());

    private CurrentMusicProvider(@NonNull Context context) {
        musicList = new ArrayList<>();

        MusicItem item = new MusicItem();
        item.data = test_url;
        item.title = "7901090557280522";
        item.artist = "jt_bjt";
        musicList.add(item);

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, Media.DURATION + " > 20000", null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null)
            return;

        cursor.moveToFirst();
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            String name = cursor.getString(0);
            String artist = cursor.getString(1);
            String path = cursor.getString(2);
            int duration = cursor.getInt(3) / 1000;
            MusicItem musicItem = new MusicItem();
            musicItem.data = path;
            musicItem.title = name;
            musicItem.artist = artist;
            musicItem.duration = duration;

            int r = random.nextInt(3);
            if (r == 0)
                musicItem.cover = url1;
            else if (r == 1)
                musicItem.cover = url2;
            else
                musicItem.cover = url3;

            musicList.add(musicItem);
            cursor.moveToNext();
        }
        cursor.close();


        TestApi testApi = RetrofitUtil.create(TestApi.class);
        testApi.get().enqueue(new Callback<M163>() {
            @Override
            public void onResponse(Call<M163> call, Response<M163> response) {
                M163 m163 = response.body();
                for (M163.ResultBean.TracksBean tracksBean : m163.result.tracks) {
                    Logger.i(tracksBean.name);
                    Logger.i(tracksBean.mp3Url);
                    Logger.i(tracksBean.artists.get(0).name);
                    Logger.i(tracksBean.duration + "");
                }
            }

            @Override
            public void onFailure(Call<M163> call, Throwable t) {

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
