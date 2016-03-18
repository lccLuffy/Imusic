package com.lcc.imusic.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.bean.MusicItem;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class LocalMusicProvider implements MusicProvider {
    List<MusicItem> localMusicList;
    private static String[] projection = {
            Media.DISPLAY_NAME,
            Media.ARTIST,
            Media.DATA,
            Media.DURATION
    };
    private static MusicProvider musicProvider;
    public static MusicProvider getMusicProvider(Context context)
    {
        if(musicProvider == null)
            musicProvider = new LocalMusicProvider(context);
        return musicProvider;
    }

    private LocalMusicProvider(@NonNull Context context)
    {
        localMusicList = new ArrayList<>();

        /*MusicItem musicItem = new MusicItem();
        musicItem.data = "http://m1.music.126.net/jt_bjt-DDWhFI9btE2b8tw==/7901090557280522.mp3";
        musicItem.title = "test";
        musicItem.artist = "test";
        musicItem.duration = 343;

        localMusicList.add(musicItem);*/
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor == null)
            return;

        cursor.moveToFirst();
        int count = cursor.getCount();
        for(int i=0;i < count;i++){
            String name = cursor.getString(0);
            String artist = cursor.getString(1);
            String path = cursor.getString(2);
            int duration = cursor.getInt(3) / 1000;
            MusicItem musicItem = new MusicItem();
            musicItem.data = path;
            musicItem.title = name;
            musicItem.artist = artist;
            musicItem.duration = duration;
            localMusicList.add(musicItem);
            cursor.moveToNext();
        }
        cursor.close();
    }
    @NonNull
    @Override
    public List<MusicItem> provideMusics() {
        return localMusicList;
    }

    @Nullable
    @Override
    public MusicItem getPlayingMusic() {
        return playingMusic;
    }

    private MusicItem playingMusic;
    @Override
    public void setPlayingMusic(MusicItem music) {
        playingMusic = music;
    }

    @Override
    public void setPlayingMusic(int index) {
        playingMusic = localMusicList.get(index);
    }
}
