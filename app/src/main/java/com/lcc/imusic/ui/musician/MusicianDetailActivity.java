package com.lcc.imusic.ui.musician;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicianDetailAdapter;
import com.lcc.imusic.base.activity.UserActivity;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.MusiciansBean;
import com.lcc.imusic.manager.NetManager_;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class MusicianDetailActivity extends UserActivity {

    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    private long id;

    private String avatar;

    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getLongExtra("id", 1);
        avatar = getIntent().getStringExtra("avatar");
        name = getIntent().getStringExtra("name");
        setAvatar(avatar);
        setUsername(name);

        MusicianDetailAdapter musicDetailAdapter = new MusicianDetailAdapter(getSupportFragmentManager(), id);
        viewPager.setAdapter(musicDetailAdapter);
        tabLayout.setupWithViewPager(viewPager);

        initData();

    }

    private void initData() {
        NetManager_.API().musicians(id)
                .enqueue(new Callback<Msg<MusiciansBean.MuiscianItem>>() {
                    @Override
                    public void onResponse(Call<Msg<MusiciansBean.MuiscianItem>> call, Response<Msg<MusiciansBean.MuiscianItem>> response) {
                        MusiciansBean.MuiscianItem muiscianItem = response.body().Result;
                        if (muiscianItem != null) {
                            setAvatar(NetManager_.DOMAIN + muiscianItem.avatar);
                            setUsername(muiscianItem.nickname);
                        }
                    }

                    @Override
                    public void onFailure(Call<Msg<MusiciansBean.MuiscianItem>> call, Throwable t) {

                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_album_detail;
    }


}
