package com.lcc.imusic.base.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public abstract class UserActivity extends PlayBarActivity {
    @Bind(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;

    @Bind(R.id.collapsingToolbarLayout)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.username)
    protected TextView username_tv;

    @Bind(R.id.avatar)
    protected ImageView avatar_iv;

    @Bind(R.id.user_bg)
    protected ImageView user_bg;

    @Bind(R.id.userInfoWrapper)
    protected LinearLayout linearLayout;

    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle("");
        collapsingToolbarLayout.setTitle("");
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListenerHelper());
    }


    public void setAvatar(String avatar) {
        Glide.with(this).load(avatar).into(avatar_iv);
    }

    public void setUsername(String username) {
        this.username = username;
        username_tv.setText(username);
    }

    public class OnOffsetChangedListenerHelper implements AppBarLayout.OnOffsetChangedListener {
        boolean avatarCanFadeOut = true, avatarCanFadeIn = false;
        boolean hasFadeOut = false, hasFadeIn = true;
        int totalScrollRange;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            totalScrollRange = appBarLayout.getTotalScrollRange();

            float positiveOffset = -verticalOffset;
            float percent = positiveOffset / totalScrollRange;


            avatarCanFadeOut = percent >= 0.65f;

            boolean bounce = avatarCanFadeIn = percent < 0.65f;

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
            collapsingToolbarLayout.setTitle(username);
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
