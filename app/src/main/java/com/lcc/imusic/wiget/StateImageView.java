package com.lcc.imusic.wiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lcc.imusic.R;
import com.lcc.imusic.service.MusicPlayManager;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class StateImageView extends ImageView implements View.OnClickListener,View.OnLongClickListener{

    private int state = MusicPlayManager.PLAY_TYPE_LOOP;

    private OnStateChangeListener onStateChangeListener;
    public StateImageView(Context context) {
        super(context);
        init();
    }
    public StateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public StateImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init()
    {
        setImageResource(state2Drawable());
        setOnClickListener(this);
        setOnLongClickListener(this);
    }
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] states = getDrawableState();
        boolean pressed = false;
        for (int state : states)
        {
            if(state == android.R.attr.state_pressed)
            {
                pressed = true;
                break;
            }
        }
        if(pressed)
        {
            setImageResource(state2PressedDrawable());
        }
        else
        {
            setImageResource(state2Drawable());
        }
    }

    @Override
    public void onClick(View v) {
        if(state == MusicPlayManager.PLAY_TYPE_LOOP)
        {
            state = MusicPlayManager.PLAY_TYPE_ONE;
        }
        else if(state == MusicPlayManager.PLAY_TYPE_ONE)
        {
            state = MusicPlayManager.PLAY_TYPE_RANDOM;
        }
        else if(state == MusicPlayManager.PLAY_TYPE_RANDOM)
        {
            state = MusicPlayManager.PLAY_TYPE_LOOP;
        }
        setImageResource(state2Drawable());
        if(onStateChangeListener != null)
            onStateChangeListener.onStateChange(state);
    }

    public void setState(int state)
    {
        this.state = state;
    }

    @DrawableRes
    private int state2Drawable()
    {
        switch (state)
        {
            case MusicPlayManager.PLAY_TYPE_ONE:
                return R.mipmap.play_icn_one;
            case MusicPlayManager.PLAY_TYPE_RANDOM:
                return R.mipmap.play_icn_random;
        }
        return R.mipmap.play_icn_loop;
    }
    @DrawableRes
    private int state2PressedDrawable()
    {
        switch (state)
        {
            case MusicPlayManager.PLAY_TYPE_ONE:
                return R.mipmap.play_icn_one_prs;
            case MusicPlayManager.PLAY_TYPE_RANDOM:
                return R.mipmap.play_icn_random_prs;
        }
        return R.mipmap.play_icn_loop_prs;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }
    public static String state2String(int state)
    {
        switch (state)
        {
            case MusicPlayManager.PLAY_TYPE_ONE:
                return "单曲循环";
            case MusicPlayManager.PLAY_TYPE_RANDOM:
                return "随机循环";
        }
        return "循环播放";
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(getContext(),state2String(state),Toast.LENGTH_SHORT).show();
        return true;
    }

    public interface OnStateChangeListener
    {
        void onStateChange(int state);
    }
}
