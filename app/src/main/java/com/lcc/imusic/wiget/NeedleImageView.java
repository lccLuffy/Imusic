package com.lcc.imusic.wiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ImageView;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class NeedleImageView extends ImageView {
    public NeedleImageView(Context context) {
        super(context);
        init();
    }

    public NeedleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NeedleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public NeedleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setPivotX(30);
        setPivotY(30);
        setRotation(-40);
    }

    private final Interpolator interpolator = new FastOutLinearInInterpolator();

    public void pause() {
        animate()
                .rotation(-40)
                .setInterpolator(interpolator)
                .setDuration(500)
                .start();
    }

    public void resume() {
        animate()
                .rotation(0)
                .setInterpolator(interpolator)
                .setDuration(500)
                .start();
    }
}
