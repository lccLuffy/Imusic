package com.lcc.imusic.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;

import com.lcc.imusic.bean.MusicItem;

import java.util.Random;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class LocalMusicProvider extends SimpleMusicProviderImpl {
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
            musicProvider = new LocalMusicProvider(context);
        return musicProvider;
    }

    Random random = new Random(System.currentTimeMillis());

    private LocalMusicProvider(@NonNull Context context) {
        super();
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
                projection, Media.DURATION + " > 20000", null, Media.DEFAULT_SORT_ORDER);
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
    }
}
