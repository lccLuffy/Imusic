package com.lcc.imusic.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;

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
            Media.DATA
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
            MusicItem musicItem = new MusicItem();
            musicItem.path = path;
            musicItem.title = name;
            musicItem.artist = artist;
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
}
