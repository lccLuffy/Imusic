package com.lcc.imusic.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class RotateImageView extends CircleImageView implements Runnable {
    private boolean canPost = true;
    private boolean running = true;

    public RotateImageView(Context context) {
        super(context);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    int width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth() / 2;
        height = getHeight() / 2;
    }

    float degree = 0.0f;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(degree, width, height);
        if (running && canPost) {
            canPost = false;
            postDelayed(this, 16);
        }
        super.onDraw(canvas);

    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
        canPost = true;
        invalidate();
    }

    @Override
    public void run() {
        canPost = true;
        degree += 0.5f;
        if (degree >= 360) {
            degree = 0.0f;
        }
        invalidate();
    }
}
