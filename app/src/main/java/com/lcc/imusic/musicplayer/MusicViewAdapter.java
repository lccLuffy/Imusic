package com.lcc.imusic.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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


    private Context context;
    public void setData(@NonNull List<MusicItem> musicItems, @NonNull Context context) {
        this.musicItems = musicItems;
        this.context = context;
        removedViews = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    public boolean rotate = true;

    public void pause(int position)
    {
        if(viewGroup == null)
            return;
        rotate = true;
        int i = viewGroup.getChildCount();
        for (int j = 0; j < i; j++)
        {
            if(viewGroup.getChildAt(j).getId() == position)
            {
                ((RotateImageView)(viewGroup.getChildAt(j))).pause();
            }
            else {
                ((RotateImageView)(viewGroup.getChildAt(j))).pause();
            }
        }
    }
    public void resume(int position)
    {
        if(viewGroup == null)
            return;
        rotate = true;
        int i = viewGroup.getChildCount();
        for (int j = 0; j < i; j++)
        {
            if(viewGroup.getChildAt(j).getId() == position)
            {
                ((RotateImageView)(viewGroup.getChildAt(j))).resume();
            }
            else
            {
                ((RotateImageView)(viewGroup.getChildAt(j))).pause();
            }
        }
    }
    @Override
    public int getCount() {
        return musicItems.size();
    }
    ViewGroup viewGroup;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if(viewGroup == null)
            viewGroup = container;
        if(!removedViews.isEmpty())
        {
            RotateImageView imageView = removedViews.remove(0);
            container.addView(imageView);
            bindData(imageView,position);
            return imageView;
        }

        RotateImageView cover = (RotateImageView) layoutInflater.inflate(R.layout.view_music_player_cover,container,false);
        container.addView(cover);
        bindData(cover,position);
        return cover;
    }

    private void bindData(RotateImageView imageView,int position)
    {
        if(!rotate)
        {
            imageView.pause();
        }
        imageView.setId(position);
        Glide.with(context).load(musicItems.get(position).cover).into(imageView);
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
