package com.lcc.imusic.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class FooterBehavior extends CoordinatorLayout.Behavior<View> {
    private boolean isHided = false;
    private boolean isHiding = false;
    private boolean isShown = true;
    private boolean isShowing = false;

    private Interpolator interpolator = new LinearInterpolator();

    private final int duration = 350;

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (!isHided && !isHiding && dy > 2) {
            hide(child);
        }
        if (!isShowing && !isShown && dy < -2) {
            show(child);
        }
    }

    private void hide(final View view) {
        isHiding = true;
        view.animate()
                .translationY(view.getHeight())
                .setDuration(duration)
                .setInterpolator(interpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isHiding = false;
                        isHided = true;
                        isShown = false;
                        view.setVisibility(View.GONE);
                    }
                }).start();
    }


    private void show(final View view) {
        isShowing = true;
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(0)
                .setDuration(duration)
                .setInterpolator(interpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isShowing = false;
                        isShown = true;
                        isHided = false;
                    }
                }).start();
    }
}
