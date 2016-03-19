package com.lcc.imusic.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.wiget.RotateImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class MusicViewAdapter extends PagerAdapter {
    private List<MusicItem> musicItems;
    private List<RotateImageView> removedViews;
    private LayoutInflater layoutInflater;
    public MusicViewAdapter(@NonNull List<MusicItem> musicItems, @NonNull Context context) {
        this.musicItems = musicItems;
        removedViews = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return musicItems.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if(!removedViews.isEmpty())
        {
            RotateImageView imageView = removedViews.remove(0);
            container.addView(imageView);
            return imageView;
        }

        RotateImageView cover = (RotateImageView) layoutInflater.inflate(R.layout.view_music_player_cover,container,false);
        container.addView(cover);
        return cover;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        RotateImageView view = (RotateImageView) object;
        container.removeView(view);
        removedViews.add(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
