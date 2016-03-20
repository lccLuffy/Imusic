package com.lcc.imusic.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.utils.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class LocalMusicProvider implements MusicProvider {
    List<MusicItem> localMusicList;
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
            musicProvider = new LocalMusicProvider(context);
        return musicProvider;
    }

    Random random = new Random(System.currentTimeMillis());

    private LocalMusicProvider(@NonNull Context context) {
        localMusicList = new ArrayList<>();

        for (Ro.MusicBean musicBean : getRo()) {
            MusicItem musicItem = new MusicItem();
            musicItem.title = musicBean.title;
            musicItem.data = "http://storage.googleapis.com/automotive-media/" + musicBean.source;
            musicItem.cover = "http://storage.googleapis.com/automotive-media/" + musicBean.image;
            musicItem.artist = musicBean.artist;
            musicItem.duration = musicBean.duration;
            localMusicList.add(musicItem);
        }


        if (true)
            return;
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
            musicItem.title = name + String.format(Locale.CHINA, " - %s", cursor.getString(4));
            musicItem.artist = artist;
            musicItem.duration = duration;

            int r = random.nextInt(3);
            if (r == 0)
                musicItem.cover = url1;
            else if (r == 1)
                musicItem.cover = url2;
            else
                musicItem.cover = url3;

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
        if (playingMusic != null)
            return playingMusic;
        if (localMusicList != null && !localMusicList.isEmpty()) {
            return localMusicList.get(0);
        }
        return null;
    }

    private MusicItem playingMusic;

    @Override
    public void setPlayingMusic(int index) {
        playingMusicIndex = index;
        playingMusic = localMusicList.get(index);
    }

    @Override
    public int getPlayingMusicIndex() {
        return playingMusicIndex;
    }

    public static class Ro {


        /**
         * title : Jazz in Paris
         * album : Jazz & Blues
         * artist : Media Right Productions
         * genre : Jazz & Blues
         * source : Jazz_In_Paris.mp3
         * image : album_art.jpg
         * trackNumber : 1
         * totalTrackCount : 6
         * duration : 103
         * site : https://www.youtube.com/audiolibrary/music
         */

        public List<MusicBean> music;

        public static class MusicBean {
            public String title;
            public String album;
            public String artist;
            public String genre;
            public String source;
            public String image;
            public int trackNumber;
            public int totalTrackCount;
            public int duration;
            public String site;
        }
    }

    List<Ro.MusicBean> getRo() {
        return Json.fromJson(json, Ro.class).music;
    }

    String json = "{\"music\" : [ \n" +
            "\t{ \"title\" : \"Jazz in Paris\",\n" +
            "\t  \"album\" : \"Jazz & Blues\",\n" +
            "\t  \"artist\" : \"Media Right Productions\",\n" +
            "\t  \"genre\" : \"Jazz & Blues\",\n" +
            "\t  \"source\" : \"Jazz_In_Paris.mp3\",\n" +
            "\t  \"image\" : \"album_art.jpg\",\n" +
            "\t  \"trackNumber\" : 1,\n" +
            "\t  \"totalTrackCount\" : 6,\n" +
            "\t  \"duration\" : 103,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"The Messenger\",\n" +
            "\t  \"album\" : \"Jazz & Blues\",\n" +
            "\t  \"artist\" : \"Silent Partner\",\n" +
            "\t  \"genre\" : \"Jazz & Blues\",\n" +
            "\t  \"source\" : \"The_Messenger.mp3\",\n" +
            "\t  \"image\" : \"album_art.jpg\",\n" +
            "\t  \"trackNumber\" : 2,\n" +
            "\t  \"totalTrackCount\" : 6,\n" +
            "\t  \"duration\" : 132,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Talkies\",\n" +
            "\t  \"album\" : \"Jazz & Blues\",\n" +
            "\t  \"artist\" : \"Huma-Huma\",\n" +
            "\t  \"genre\" : \"Jazz & Blues\",\n" +
            "\t  \"source\" : \"Talkies.mp3\",\n" +
            "\t  \"image\" : \"album_art.jpg\",\n" +
            "\t  \"trackNumber\" : 3,\n" +
            "\t  \"totalTrackCount\" : 6,\n" +
            "\t  \"duration\" : 162,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"On the Bach\",\n" +
            "\t  \"album\" : \"Cinematic\",\n" +
            "\t  \"artist\" : \"Jingle Punks\",\n" +
            "\t  \"genre\" : \"Cinematic\",\n" +
            "\t  \"source\" : \"On_the_Bach.mp3\",\n" +
            "\t  \"image\" : \"album_art.jpg\",\n" +
            "\t  \"trackNumber\" : 4,\n" +
            "\t  \"totalTrackCount\" : 6,\n" +
            "\t  \"duration\" : 66,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"The Story Unfolds\",\n" +
            "\t  \"album\" : \"Cinematic\",\n" +
            "\t  \"artist\" : \"Jingle Punks\",\n" +
            "\t  \"genre\" : \"Cinematic\",\n" +
            "\t  \"source\" : \"The_Story_Unfolds.mp3\",\n" +
            "\t  \"image\" : \"album_art.jpg\",\n" +
            "\t  \"trackNumber\" : 5,\n" +
            "\t  \"totalTrackCount\" : 6,\n" +
            "\t  \"duration\" : 91,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Drop and Roll\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock\",\n" +
            "\t  \"artist\" : \"Silent Partner\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Drop_and_Roll.mp3\",\n" +
            "\t  \"image\" : \"album_art_2.jpg\",\n" +
            "\t  \"trackNumber\" : 1,\n" +
            "\t  \"totalTrackCount\" : 7,\n" +
            "\t  \"duration\" : 121,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Motocross\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock\",\n" +
            "\t  \"artist\" : \"Topher Mohr and Alex Elena\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Motocross.mp3\",\n" +
            "\t  \"image\" : \"album_art_2.jpg\",\n" +
            "\t  \"trackNumber\" : 2,\n" +
            "\t  \"totalTrackCount\" : 7,\n" +
            "\t  \"duration\" : 182,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Wish You'd Come True\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock\",\n" +
            "\t  \"artist\" : \"The 126ers\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Wish_You_d_Come_True.mp3\",\n" +
            "\t  \"image\" : \"album_art_2.jpg\",\n" +
            "\t  \"trackNumber\" : 3,\n" +
            "\t  \"totalTrackCount\" : 7,\n" +
            "\t  \"duration\" : 169,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Awakening\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock\",\n" +
            "\t  \"artist\" : \"Silent Partner\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Awakening.mp3\",\n" +
            "\t  \"image\" : \"album_art_2.jpg\",\n" +
            "\t  \"trackNumber\" : 4,\n" +
            "\t  \"totalTrackCount\" : 7,\n" +
            "\t  \"duration\" : 220,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Home\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock\",\n" +
            "\t  \"artist\" : \"Letter Box\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Home.mp3\",\n" +
            "\t  \"image\" : \"album_art_2.jpg\",\n" +
            "\t  \"trackNumber\" : 5,\n" +
            "\t  \"totalTrackCount\" : 7,\n" +
            "\t  \"duration\" : 213,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Tell The Angels\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock\",\n" +
            "\t  \"artist\" : \"Letter Box\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Tell_The_Angels.mp3\",\n" +
            "\t  \"image\" : \"album_art_2.jpg\",\n" +
            "\t  \"trackNumber\" : 6,\n" +
            "\t  \"totalTrackCount\" : 7,\n" +
            "\t  \"duration\" : 208,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Hey Sailor\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock\",\n" +
            "\t  \"artist\" : \"Letter Box\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Hey_Sailor.mp3\",\n" +
            "\t  \"image\" : \"album_art_2.jpg\",\n" +
            "\t  \"trackNumber\" : 7,\n" +
            "\t  \"totalTrackCount\" : 7,\n" +
            "\t  \"duration\" : 193,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"Keys To The Kingdom\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock 2\",\n" +
            "\t  \"artist\" : \"The 126ers\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"Keys_To_The_Kingdom.mp3\",\n" +
            "\t  \"image\" : \"album_art_3.jpg\",\n" +
            "\t  \"trackNumber\" : 1,\n" +
            "\t  \"totalTrackCount\" : 2,\n" +
            "\t  \"duration\" : 221,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t},\n" +
            "\t{ \"title\" : \"The Coldest Shoulder\",\n" +
            "\t  \"album\" : \"Youtube Audio Library Rock 2\",\n" +
            "\t  \"artist\" : \"The 126ers\",\n" +
            "\t  \"genre\" : \"Rock\",\n" +
            "\t  \"source\" : \"The_Coldest_Shoulder.mp3\",\n" +
            "\t  \"image\" : \"album_art_3.jpg\",\n" +
            "\t  \"trackNumber\" : 2,\n" +
            "\t  \"totalTrackCount\" : 2,\n" +
            "\t  \"duration\" : 160,\n" +
            "\t  \"site\" : \"https://www.youtube.com/audiolibrary/music\"\n" +
            "\t}\n" +
            "]}";
}
