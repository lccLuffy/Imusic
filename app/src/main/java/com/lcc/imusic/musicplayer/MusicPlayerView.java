package com.lcc.imusic.musicplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.wiget.NeedleImageView;
import com.lcc.imusic.wiget.StateImageView;

import java.util.List;
import java.util.Locale;

/**
 * Created by lcc_luffy on 2016/3/16.
 */
public class MusicPlayerView extends FrameLayout implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {

    private ViewPager viewPager;

    private MusicViewAdapter musicViewAdapter;

    private CheckBox cb_play;

    private SeekBar seekBar;

    private TextView tv_totalTime;
    private TextView tv_currentTime;

    private NeedleImageView needleImageView;

    private StateImageView stateImageView;

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

        needleImageView = (NeedleImageView) panel.findViewById(R.id.musicView_needleImageView);

        tv_totalTime = (TextView) panel.findViewById(R.id.musicView_totalTime);
        tv_currentTime = (TextView) panel.findViewById(R.id.musicView_currentTime);

        seekBar = (SeekBar) panel.findViewById(R.id.musicView_seekBar);

        cb_play = (CheckBox) panel.findViewById(R.id.musicView_play);
        ImageView iv_prev = (ImageView) panel.findViewById(R.id.musicView_prev);
        ImageView iv_next = (ImageView) panel.findViewById(R.id.musicView_next);
        ImageView musicView_src = (ImageView) panel.findViewById(R.id.musicView_src);

        viewPager = (ViewPager) panel.findViewById(R.id.musicView_viewPage);



        stateImageView = (StateImageView) panel.findViewById(R.id.musicView_playState);
        stateImageView.setOnStateChangeListener(new StateImageView.OnStateChangeListener() {
            @Override
            public void onStateChange(int playType) {
                if(musicPlayerCallBack != null)
                    musicPlayerCallBack.onPlayTypeChange(playType);
            }
        });

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
                isUserSliding = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSliding = false;
                musicPlayerCallBack.onSliderFinished(seekBar.getProgress());
            }
        });
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
        if(!state)
        {
            provideMusicViewAdapter().rotate = false;
        }
        if(cb_play.isChecked() != state)
        {
            fromUser = true;
            cb_play.setChecked(state);
        }
    }
    public void setPlayType(int playType)
    {
        stateImageView.setState(playType);
    }

    public void setProgress(int second)
    {
        seekBar.setProgress(second);
        setCurrentTime(second);
    }

    public void setSecondaryProgress(int percent)
    {
        seekBar.setSecondaryProgress((int) (percent * 1.0f / 100 * seekBar.getMax()));
    }

    public void setPageIndex(int index)
    {
        viewPager.setCurrentItem(index);
        this.currentPlayingMusic = index;
    }

    private int currentPlayingMusic;
    public void setMusicList(@NonNull List<MusicItem> musicList,int currentPlayingMusic)
    {
        this.currentPlayingMusic = currentPlayingMusic;
        provideMusicViewAdapter().setData(musicList,getContext());
        viewPager.setAdapter(provideMusicViewAdapter());
        viewPager.addOnPageChangeListener(new OnPageSelected());
        viewPager.setCurrentItem(currentPlayingMusic);
    }

    @NonNull
    private MusicViewAdapter provideMusicViewAdapter()
    {
        if(musicViewAdapter == null)
            musicViewAdapter = new MusicViewAdapter();
        return musicViewAdapter;
    }

    private class OnPageSelected implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            /*if(musicPlayerCallBack != null && position != currentPlayingMusic)
            {
                musicPlayerCallBack.onPageSelected(position);
            }*/
            currentPlayingMusic = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state != ViewPager.SCROLL_STATE_IDLE)
            {
                needleImageView.pause();
            }
            else
            {
                needleImageView.resume();
            }
        }
    }

    public void setTotalProgress(int totalProgress)
    {
        seekBar.setMax(totalProgress);
        setTotalTime(totalProgress);
    }


    private boolean isUserSliding = false;
    public boolean isUserSliding()
    {
        return isUserSliding;
    }

    public boolean isPaused()
    {
        return !cb_play.isChecked();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(fromUser)
        {
            if(isChecked)
            {
                provideMusicViewAdapter().resume(viewPager.getCurrentItem());
                needleImageView.quickResume();
            }
            else
            {
                provideMusicViewAdapter().pause(viewPager.getCurrentItem());
                needleImageView.quickPause();
            }
            fromUser = false;
            return;
        }

        if(isChecked)
        {
            provideMusicViewAdapter().resume(viewPager.getCurrentItem());
            needleImageView.resume();
        }
        else
        {
            provideMusicViewAdapter().pause(viewPager.getCurrentItem());
            needleImageView.pause();
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
                    setPageIndex(currentPlayingMusic - 1);
                    break;
                case R.id.musicView_next:
                    setPageIndex(currentPlayingMusic + 1);
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
        /**
         * 开始按钮被按下
         */
        void start();

        /**
         * 暂停按钮被按下
         */
        void pause();

        /**
         * 下一首按钮被按下
         */
        void next();

        /**
         * 上一首按钮被按下
         */
        void prev();

        /**
         * 播放列表按钮被按下
         */
        void onShowMusicSrc();

        /**
         * 进度条进度改变
         * @param second
         */
        void onSliderChanged(int second);

        /**
         * 进度条滑动完成
         * @param currentSecond
         */
        void onSliderFinished(int currentSecond);

        /**
         * LOOP,ONE,RANDOM
         * @param playType
         */
        void onPlayTypeChange(int playType);

        /**
         * ViewPage 滑动时调用
         * @param position
         */
        void onPageSelected(int position);

    }
}
