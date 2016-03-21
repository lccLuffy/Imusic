package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.SimpleMusicListAdapter;
import com.lcc.imusic.base.AttachFragment;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.OnMusicList;
import com.lcc.imusic.model.RemoteMusicProvider;

import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class ActivitiesFragment extends AttachFragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    SimpleMusicListAdapter simpleMusicListAdapter;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        simpleMusicListAdapter = new SimpleMusicListAdapter();
        recyclerView.setAdapter(simpleMusicListAdapter);
        /*simpleMusicListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mainActivity.playMusic(position);
            }
        });*/
        RemoteMusicProvider.getData(new OnMusicList() {
            @Override
            public void onSuccess(List<MusicItem> musicItems) {
                simpleMusicListAdapter.setData(musicItems);
            }

            @Override
            public void onFail(String reason) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    public String toString() {
        return "活动";
    }
}
