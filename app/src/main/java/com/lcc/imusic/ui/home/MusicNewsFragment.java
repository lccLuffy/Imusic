package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicNewsAdapter;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.MusicNews;
import com.lcc.imusic.wiget.StateLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class MusicNewsFragment extends AttachFragment implements OnRefreshListener {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    MusicNewsAdapter musicNewsAdapter;

    static List<MusicNews> list;

    static {
        list = new ArrayList<>();
        list.add(new MusicNews("电影《纽约纽约》主题曲MV曝光 徐佳莹颠覆献唱",
                "http://img1.gtimg.com/ent/pics/hv1/156/138/2040/132686346.jpg",
                "腾讯娱乐讯 电影《纽约纽约》于今日曝光了主题曲《潇洒走一回》的MV，当红华乐坛实力女唱将徐佳莹重新演绎了这首90年代的经典"));

        list.add(new MusicNews("容祖儿惊艳《歌手》引海外粉丝高度关注",
                "http://img1.gtimg.com/15/1552/155284/15528489_980x1200_1202.jpg",
                "腾讯娱乐讯 自2 月26日晚，容祖儿凭借《月半小夜曲》惊艳补位，到《想着你的感觉》，" +
                        "嗨翻全场的《煞科》，再到上一场的《突然想爱你》，容祖儿用歌喉展现了天后实力"));

        list.add(new MusicNews("电影《纽约纽约》主题曲MV曝光 徐佳莹颠覆献唱",
                "http://img1.gtimg.com/ent/pics/hv1/156/138/2040/132686346.jpg",
                "腾讯娱乐讯 电影《纽约纽约》于今日曝光了主题曲《潇洒走一回》的MV，当红华乐坛实力女唱将徐佳莹重新演绎了这首90年代的经典"));

        list.add(new MusicNews("电影《纽约纽约》主题曲MV曝光 徐佳莹颠覆献唱",
                "http://img1.gtimg.com/ent/pics/hv1/156/138/2040/132686346.jpg",
                "腾讯娱乐讯 电影《纽约纽约》于今日曝光了主题曲《潇洒走一回》的MV，当红华乐坛实力女唱将徐佳莹重新演绎了这首90年代的经典"));

        list.add(new MusicNews("Sunshine唱歌进步惊人 网友大赞新歌：好听到哭",
                "http://img1.gtimg.com/ent/pics/hv1/119/129/2040/132684014.jpg",
                "腾讯娱乐讯 Sunshine的新歌《两小无猜》22日透过微博首次公开，该曲是翻唱大陆歌手陈秋实、蔡照于2015年发行的歌曲。"));
    }

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        refreshLayout.setOnRefreshListener(this);
        musicNewsAdapter = new MusicNewsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(musicNewsAdapter);
        getData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    boolean first = true;

    private void getData() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                musicNewsAdapter.addData(list);
                refreshLayout.setRefreshing(false);
            }
        }, first ? 0 : 2000);
        first = false;
    }

    @Override
    public String toString() {
        return "音乐资讯";
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
