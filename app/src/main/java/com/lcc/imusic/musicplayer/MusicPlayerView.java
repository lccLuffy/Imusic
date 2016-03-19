package com.lcc.imusic.musicplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.wiget.NeedleImageView;
import com.lcc.imusic.wiget.RotateImageView;

import java.util.Locale;

/**
 * Created by lcc_luffy on 2016/3/16.
 */
public class MusicPlayerView extends FrameLayout implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {


    private RotateImageView iv_cover;

    private CheckBox cb_play;



    private SeekBar seekBar;

    private TextView tv_totalTime;
    private TextView tv_currentTime;

    private NeedleImageView needleImageView;

    private MusicPlayerCallBack musicPlayerCallBack;

    private final String test_url = "http://m1.music.126.net/jt_bjt-DDWhFI9btE2b8tw==/7901090557280522.mp3";

    public MusicPlayerView(Context context) {
        super(context);
        init();
    }

    public MusicPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MusicPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MusicPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public void setMusicPlayerCallBack(MusicPlayerCallBack musicPlayerCallBack)
    {
        this.musicPlayerCallBack = musicPlayerCallBack;
    }

    private void init() {
        View panel = LayoutInflater.from(getContext()).inflate(R.layout.view_music_player, this, false);
        addView(panel);

        iv_cover = (RotateImageView) panel.findViewById(R.id.musicView_cover);
        needleImageView = (NeedleImageView) panel.findViewById(R.id.musicView_needleImageView);


        tv_totalTime = (TextView) panel.findViewById(R.id.musicView_totalTime);
        tv_currentTime = (TextView) panel.findViewById(R.id.musicView_currentTime);

        seekBar = (SeekBar) panel.findViewById(R.id.musicView_seekBar);

        cb_play = (CheckBox) panel.findViewById(R.id.musicView_play);
        ImageView iv_prev = (ImageView) panel.findViewById(R.id.musicView_prev);
        ImageView iv_next = (ImageView) panel.findViewById(R.id.musicView_next);
        ImageView musicView_src = (ImageView) panel.findViewById(R.id.musicView_src);


        iv_prev.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        musicView_src.setOnClickListener(this);

        cb_play.setOnCheckedChangeListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && musicPlayerCallBack != null) {
                    musicPlayerCallBack.onSliderChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicPlayerCallBack.onSliderFinished(seekBar.getProgress());
            }
        });
    }

    public void setCover(String url) {
        Glide.with(getContext()).load(url).into(iv_cover);
    }

    /**
     * in second
     * @param totalTime
     */
    private void setTotalTime(int totalTime)
    {
        tv_totalTime.setText(String.format(Locale.CHINA, "%d:%02d", totalTime / 60, totalTime % 60));
    }
    /**
     * in second
     * @param currentTime
     */
    private void setCurrentTime(int currentTime)
    {
        tv_currentTime.setText(String.format(Locale.CHINA, "%d:%02d", currentTime / 60, currentTime % 60));
    }

    private boolean fromUser = false;
    public void setPlayBtnState(boolean state)
    {
        if(cb_play.isChecked() != state)
        {
            fromUser = true;
            cb_play.setChecked(state);
        }
    }

    public void setProgress(int second)
    {
        /*if(Math.abs(second - seekBar.getProgress()) > 10)
        {
            ObjectAnimator animator = ObjectAnimator
                    .ofInt(seekBar,"progress",second)
                    .setDuration(100);
            animator.start();
        }*/
        seekBar.setProgress(second);
        setCurrentTime(second);
    }

    public void setTotalProgress(int totalProgress)
    {
        seekBar.setMax(totalProgress);
        setTotalTime(totalProgress);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(fromUser )
        {
            if(isChecked)
            {
                iv_cover.resume();
                needleImageView.quickResume();
            }
            else
            {
                iv_cover.pause();
                needleImageView.quickPause();
            }
            fromUser = false;
            return;
        }

        if(isChecked)
        {
            iv_cover.resume();
            needleImageView.quickResume();
        }
        else
        {
            iv_cover.pause();
            needleImageView.quickPause();
        }

        if(musicPlayerCallBack != null)
        {
            if (isChecked) {
                musicPlayerCallBack.start();
            } else {
                musicPlayerCallBack.pause();
            }
        }

    }
    @Override
    public void onClick(View v)
    {
        if (musicPlayerCallBack != null)
        {
            switch (v.getId())
            {
                case R.id.musicView_prev:
                    musicPlayerCallBack.prev();
                    break;
                case R.id.musicView_next:
                    musicPlayerCallBack.next();
                    break;
                case R.id.musicView_src:
                    musicPlayerCallBack.onShowMusicSrc();
                    break;
            }
        }
    }
    public interface MusicPlayerCallBack
    {
        void start();
        void pause();
        void next();
        void prev();
        void onShowMusicSrc();
        void onSliderChanged(int second);
        void onSliderFinished(int currentSecond);
    }
}
