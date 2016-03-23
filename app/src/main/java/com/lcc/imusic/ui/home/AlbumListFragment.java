package com.lcc.imusic.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicGridAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.Album;
import com.lcc.imusic.ui.AlbumDetailActivity;
import com.lcc.stateLayout.StateLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class AlbumListFragment extends AttachFragment {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;


    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    MusicGridAdapter musicGridAdapter;


    static List<Album> albumList;

    static {
        albumList = new ArrayList<>();
        albumList.add(new Album(317054928, "http://p3.music.126.net/UF_6h6Bp8E6Z1JEMy6tpOQ==/1374389537678755.jpg?param=90y90", "【前任】时光岁月，抹煞眉眼"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        refreshLayout.setEnabled(false);
        musicGridAdapter = new MusicGridAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(musicGridAdapter);
        musicGridAdapter.setData(albumList);
        musicGridAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(context, AlbumDetailActivity.class));
            }
        });
    }
}
