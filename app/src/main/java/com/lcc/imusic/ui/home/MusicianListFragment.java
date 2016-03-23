package com.lcc.imusic.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicianRankAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.Musician;
import com.lcc.imusic.ui.MusicianDetailActivity;
import com.lcc.imusic.wiget.StateLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class MusicianListFragment extends AttachFragment {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;


    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    MusicianRankAdapter musicGridAdapter;


    static List<Musician> albumList;

    static {
        albumList = new ArrayList<>();
        albumList.add(new Musician(317054928, "http://img.xiami.net/images/artistlogo/17/14181897004117.jpg", "陈粒"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/81/13770678042781.jpg", "李荣浩"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/47/14116279498547.jpg", "金海心"));
        albumList.add(new Musician(306397077, "http://img.xiami.net/images/artistlogo/81/14198493321381.jpg", "邵夷贝"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/47/14116279498547.jpg", "金海心"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/47/14116279498547.jpg", "金海心"));
        albumList.add(new Musician(58451795, "http://img.xiami.net/images/artistlogo/47/14116279498547.jpg", "金海心"));
        /*albumList.add(new Musician(1, "http://p3.music.126.net/UF_6h6Bp8E6Z1JEMy6tpOQ==/1374389537678755.jpg?param=90y90", "【前任】时光岁月，抹煞眉眼"));
        albumList.add(new Musician(317054928, "http://p3.music.126.net/UF_6h6Bp8E6Z1JEMy6tpOQ==/1374389537678755.jpg?param=90y90", "【前任】时光岁月，抹煞眉眼"));
        albumList.add(new Musician(5845662, "http://p3.music.126.net/UF_6h6Bp8E6Z1JEMy6tpOQ==/1374389537678755.jpg?param=90y90", "【前任】时光岁月，抹煞眉眼"));
        albumList.add(new Musician(317054928, "http://p3.music.126.net/UF_6h6Bp8E6Z1JEMy6tpOQ==/1374389537678755.jpg?param=90y90", "【前任】时光岁月，抹煞眉眼"));
        albumList.add(new Musician(306397077, "http://p3.music.126.net/UF_6h6Bp8E6Z1JEMy6tpOQ==/1374389537678755.jpg?param=90y90", "【前任】时光岁月，抹煞眉眼"));*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        refreshLayout.setEnabled(false);
        musicGridAdapter = new MusicianRankAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(musicGridAdapter);
        musicGridAdapter.setData(albumList);
        musicGridAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(context, MusicianDetailActivity.class);
                i.putExtra("id", musicGridAdapter.getData(position).id);
                startActivity(i);
            }
        });
    }
}
