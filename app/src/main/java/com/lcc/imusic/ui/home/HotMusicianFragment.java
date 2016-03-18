package com.lcc.imusic.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicItemAdapter;
import com.lcc.imusic.base.BaseFragment;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.StateRecyclerView;

import butterknife.Bind;


/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class HotMusicianFragment extends BaseFragment {

    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    MusicItemAdapter musicItemAdapter;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        musicItemAdapter = new MusicItemAdapter(context);
        stateRecyclerView.setAdapter(musicItemAdapter);
        musicItemAdapter.getLoadMoreFooter().showNoMoreView();
        stateRecyclerView.setEnabled(false);
        musicItemAdapter.setOnItemClickListener(new NiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("music", musicItemAdapter.getData(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    public String toString() {
        return "热门音乐人";
    }
}
