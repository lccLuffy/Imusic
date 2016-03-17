package com.lcc.imusic.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.utils.L;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lcc_luffy on 2016/3/16.
 */
public class MusicPlayerView extends FrameLayout implements CompoundButton.OnCheckedChangeListener{

    private MediaPlayer mediaPlayer;

    private Animation animation;

    private ImageView iv_cover;

    private CheckBox cb_play;
    private ImageView iv_prev;
    private ImageView iv_next;


    private SeekBar seekBar;

    private TextView tv_totalTime;
    private TextView tv_currentTime;

    private Timer timer;


    private final String test_url = "http://m1.music.126.net/jt_bjt-DDWhFI9btE2b8tw==/7901090557280522.mp3";

    public static class MusicItem implements Serializable
    {
        public String title;
        public String path;
        public String artist;
    }

    private MusicItem musicItem;
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

    private void init() {
        View panel = LayoutInflater.from(getContext()).inflate(R.layout.view_music_player, this, false);
        addView(panel);

        iv_cover = (ImageView) panel.findViewById(R.id.cover);
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(10000);
        animation.setInterpolator(new LinearInterpolator());
        iv_cover.startAnimation(animation);


        tv_totalTime = (TextView) panel.findViewById(R.id.totalTime);
        tv_currentTime = (TextView) panel.findViewById(R.id.currentTime);

        seekBar = (SeekBar) panel.findViewById(R.id.seekBar);

        cb_play = (CheckBox) panel.findViewById(R.id.btn_play);
        iv_prev = (ImageView) panel.findViewById(R.id.btn_prev);
        iv_next = (ImageView) panel.findViewById(R.id.btn_next);

        cb_play.setOnCheckedChangeListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void play()
    {
        cb_play.setChecked(true);
    }

    public void setData(MusicItem musicItem)
    {
        this.musicItem = musicItem;
        if(mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
        }
        else
        {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
        }
        try
        {
            mediaPlayer.setDataSource(musicItem.path);
            mediaPlayer.prepare();

            timer = new Timer();
            MediaListener mediaListener = new MediaListener();

            mediaPlayer.setOnCompletionListener(mediaListener);
            mediaPlayer.setOnPreparedListener(mediaListener);
            mediaPlayer.setOnErrorListener(mediaListener);
            mediaPlayer.setOnBufferingUpdateListener(mediaListener);
            mediaPlayer.setOnSeekCompleteListener(mediaListener);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setCover(String url)
    {
        Glide.with(getContext()).load(url).into(iv_cover);
    }
    private void start()
    {
        if(musicItem == null)
        {
            return;
        }
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }
    private void pause()
    {
        if(mediaPlayer != null)
            mediaPlayer.pause();
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            start();
        }
        else
        {
            pause();
        }
    }

    private class Task extends TimerTask
    {
        @Override
        public void run() {
            final int current = mediaPlayer.getCurrentPosition() / 1000;
            final String text = String.format(Locale.CHINA,"%d:%02d",current / 60,current % 60);
            post(new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(current);
                    tv_currentTime.setText(text);
                }
            });
        }
    }
    private class MediaListener implements MediaPlayer.OnCompletionListener
            ,MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener
            ,MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnSeekCompleteListener
    {

        @Override
        public void onCompletion(MediaPlayer mp) {

        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            int second = mediaPlayer.getDuration() / 1000;
            tv_totalTime.setText(String.format(Locale.CHINA,"%d:%02d",second / 60,second % 60));

            seekBar.setMax(second);
            timer.schedule(new Task(),0,1000);
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            L.i(what+"");
            return false;
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            seekBar.setSecondaryProgress(percent);
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            final int current = mediaPlayer.getCurrentPosition() / 1000;
            final String text = String.format(Locale.CHINA,"%d:%02d",current / 60,current % 60);
            seekBar.setProgress(current);
            tv_currentTime.setText(text);
        }
    }
}
