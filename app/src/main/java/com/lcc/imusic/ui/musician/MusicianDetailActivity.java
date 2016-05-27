package com.lcc.imusic.ui.musician;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicianDetailAdapter;
import com.lcc.imusic.base.activity.UserActivity;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.MusicianItem;
import com.lcc.imusic.manager.NetManager_;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class MusicianDetailActivity extends UserActivity {

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
        NetManager_.API().musician(id)
                .enqueue(new Callback<Msg<MusicianItem>>() {
                    @Override
                    public void onResponse(Call<Msg<MusicianItem>> call, Response<Msg<MusicianItem>> response) {
                        MusicianItem musicianItem = response.body().Result;
                        if (musicianItem != null) {
                            setAvatar(NetManager_.DOMAIN + musicianItem.avatar);
                            setUsername(musicianItem.nickname);
                            Glide.with(MusicianDetailActivity.this).load(NetManager_.DOMAIN + musicianItem.IDphotopath).into(user_bg);
                        }
                    }

                    @Override
                    public void onFailure(Call<Msg<MusicianItem>> call, Throwable t) {

                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_album_detail;
    }


}
