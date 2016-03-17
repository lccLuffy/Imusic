package com.lcc.imusic;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.AccountActivity;
import com.lcc.imusic.musicplayer.MusicPlayerView;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.lcc.imusic.ui.setting.SettingActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends AccountActivity {
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private static String[] projection = {
            Media._ID,
            Media.DISPLAY_NAME,
            Media.DATA,
            Media.ALBUM,
            Media.ARTIST,
            Media.DURATION,
            Media.SIZE,
    };
    private static String where =  "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 " ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setAvatar("http://static.oschina.net/uploads/img/201304/17033908_N1hN.jpg");

        /*TestApi testApi = RetrofitUtil.create(TestApi.class);
        testApi.getSongs().enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                for (Song.MusicsEntity musicsEntity : response.body().musics)
                {
                    L.i(musicsEntity.title);
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {

            }
        });*/

        s();
    }


    void s()
    {
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            String name = cursor.getString(1);
            String artist = cursor.getString(4);
            String path = cursor.getString(2);
            MusicPlayerView.MusicItem musicItem = new MusicPlayerView.MusicItem();
            musicItem.path = path;
            musicItem.title = name;
            musicItem.artist = artist;
            Intent intent = new Intent(this,MusicPlayerActivity.class);
            intent.putExtra("music",musicItem);
            startActivity(intent);
            break;
        }
        cursor.close();
    }
    private void init()
    {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public List<IDrawerItem> onCreateMenuItem() {
        List<IDrawerItem> list = new ArrayList<>();
        list.add(new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home));
        list.add(new PrimaryDrawerItem().withName("Music").withIcon(FontAwesome.Icon.faw_music));
        return list;
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        if(position == 2)
        {
            startActivity(new Intent(this, MusicPlayerActivity.class));
            return true;
        }
        return super.onDrawerMenuSelected(view, position, drawerItem);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
