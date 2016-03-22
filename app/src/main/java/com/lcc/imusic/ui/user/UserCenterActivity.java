package com.lcc.imusic.ui.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class UserCenterActivity extends BaseActivity {
    @Bind(R.id.username)
    TextView username_tv;

    @Bind(R.id.email)
    TextView email_tv;

    @Bind(R.id.avatar)
    ImageView avatar_iv;

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.viewpagerTab)
    SmartTabLayout viewpagerTab;

    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Bind(R.id.postsCount)
    TextView postsCount;

    @Bind(R.id.userInfoWrapper)
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayShowTitleEnabled(false);
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListenerHelper());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
    }
    private class OnOffsetChangedListenerHelper implements AppBarLayout.OnOffsetChangedListener
    {
        boolean avatarCanFadeOut = true,avatarCanFadeIn = false;
        boolean hasFadeOut = false,hasFadeIn = true;
        int totalScrollRange;
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            totalScrollRange = appBarLayout.getTotalScrollRange();

            float positiveOffset = -verticalOffset;
            float percent = positiveOffset / totalScrollRange;

            avatarCanFadeOut = percent >= 0.65f;

            avatarCanFadeIn = percent < 0.65f;

            if(!hasFadeOut && avatarCanFadeOut)
            {
                hasFadeOut = true;
                animateOut(linearLayout);
                /*animateOut(username_tv);*/

            }
            else if(!hasFadeIn && avatarCanFadeIn)
            {
                hasFadeIn = true;
                animateIn(linearLayout);
                /*animateIn(username_tv);*/
            }
        }



        private void animateOut(View target)
        {
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
        private void animateIn(View target)
        {
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
