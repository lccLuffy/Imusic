package com.lcc.imusic.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicianDetailAdapter;
import com.lcc.imusic.base.activity.PlayBarActivity;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class MusicianDetailActivity extends PlayBarActivity {

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.username)
    TextView username_tv;

    @Bind(R.id.avatar)
    ImageView avatar_iv;

    @Bind(R.id.userInfoWrapper)
    LinearLayout linearLayout;

    String name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("id", 1);
        actionBar.setTitle("");
        collapsingToolbarLayout.setTitle("");
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListenerHelper());

        MusicianDetailAdapter musicDetailAdapter = new MusicianDetailAdapter(getSupportFragmentManager());
        viewPager.setAdapter(musicDetailAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setup("http://img.xiami.net/images/artistlogo/41/13739635501641.jpg", "庄心妍");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_album_detail;
    }


    private void setup(String avatar, String name) {
        Glide.with(MusicianDetailActivity.this)
                .load(avatar)
                .placeholder(R.mipmap.msg_icn_user)
                .into(avatar_iv);
        this.name = name;
        username_tv.setText(name);
    }


    private class OnOffsetChangedListenerHelper implements AppBarLayout.OnOffsetChangedListener {
        boolean avatarCanFadeOut = true, avatarCanFadeIn = false;
        boolean hasFadeOut = false, hasFadeIn = true;
        int totalScrollRange;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            totalScrollRange = appBarLayout.getTotalScrollRange();

            float positiveOffset = -verticalOffset;
            float percent = positiveOffset / totalScrollRange;

            avatarCanFadeOut = percent >= 0.65f;

            avatarCanFadeIn = percent < 0.65f;

            if (!hasFadeOut && avatarCanFadeOut) {
                hasFadeOut = true;
                animateOut(linearLayout);
                /*animateOut(username_tv);*/

            } else if (!hasFadeIn && avatarCanFadeIn) {
                hasFadeIn = true;
                animateIn(linearLayout);
                /*animateIn(username_tv);*/
            }
        }


        private void animateOut(View target) {
            collapsingToolbarLayout.setTitle(name);
            target.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .alpha(0)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hasFadeIn = false;

                        }
                    })
                    .start();
        }

        private void animateIn(View target) {
            collapsingToolbarLayout.setTitle(null);
            target.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .alpha(1)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hasFadeOut = false;

                        }
                    })
                    .start();
        }
    }
}
