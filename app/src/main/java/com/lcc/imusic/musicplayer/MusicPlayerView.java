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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.imusic.R;
import com.lcc.imusic.wiget.NeedleImageView;
import com.lcc.imusic.wiget.RotateImageView;
import com.lcc.imusic.wiget.StateImageView;

import java.util.Locale;

/**
 * Created by lcc_luffy on 2016/3/16.
 */
public class MusicPlayerView extends FrameLayout implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox cb_play;

    private RotateImageView cover;

    private SeekBar seekBar;

    private TextView tv_totalTime;
    private TextView tv_currentTime;

    private NeedleImageView needleImageView;

    private StateImageView stateImageView;

    private MusicPlayerCallBack musicPlayerCallBack;

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

    public void setMusicPlayerCallBack(MusicPlayerCallBack musicPlayerCallBack) {
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

        cover = (RotateImageView) panel.findViewById(R.id.musicView_cover);

        stateImageView = (StateImageView) panel.findViewById(R.id.musicView_playState);
        stateImageView.setOnStateChangeListener(new StateImageView.OnStateChangeListener() {
            @Override
            public void onStateChange(int playType) {
                if (musicPlayerCallBack != null)
                    musicPlayerCallBack.onPlayTypeChange(playType);
            }
        });

        View download = panel.findViewById(R.id.musicView_dl);
        download.setOnClickListener(this);

        panel.findViewById(R.id.musicView_cmt).setOnClickListener(this);

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
     *
     * @param totalTime
     */
    private void setTotalTime(int totalTime) {
        tv_totalTime.setText(String.format(Locale.CHINA, "%d:%02d", totalTime / 60, totalTime % 60));
    }

    /**
     * in second
     *
     * @param currentTime
     */
    private void setCurrentTime(int currentTime) {
        tv_currentTime.setText(String.format(Locale.CHINA, "%d:%02d", currentTime / 60, currentTime % 60));
    }

    public void setCover(final String cover_url) {

        Glide.with(getContext())
                .load(cover_url)
                .placeholder(R.mipmap.placeholder_disk_play_song)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(cover);

    }

    private boolean fromUser = false;

    public void setPlayBtnState(boolean state) {
        if (state) {
            cover.resume();
        } else {
            cover.pause();
        }
        if (cb_play.isChecked() != state) {
            fromUser = true;
            cb_play.setChecked(state);
        }
    }

    public void setPlayType(int playType) {
        stateImageView.setState(playType);
    }

    public void setProgress(int second) {
        seekBar.setProgress(second);
        setCurrentTime(second);
    }

    public void setSecondaryProgress(int percent) {
        seekBar.setSecondaryProgress((int) (percent * 1.0f / 100 * seekBar.getMax()));
    }

    public void setTotalProgress(int totalProgress) {
        seekBar.setMax(totalProgress);
        setTotalTime(totalProgress);
    }


    private boolean isUserSliding = false;

    public boolean isUserSliding() {
        return isUserSliding;
    }

    public boolean isPaused() {
        return !cb_play.isChecked();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (fromUser) {
            fromUser = false;
            if (isChecked) {
                cover.resume();
                needleImageView.quickResume();
            } else {
                cover.pause();
                needleImageView.quickPause();
            }
            return;
        }

        if (isChecked) {
            cover.resume();
            needleImageView.resume();
        } else {
            cover.pause();
            needleImageView.pause();
        }

        if (musicPlayerCallBack != null) {
            if (isChecked) {
                musicPlayerCallBack.start();
            } else {
                musicPlayerCallBack.pause();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (musicPlayerCallBack != null) {
            switch (v.getId()) {
                case R.id.musicView_prev:
                    musicPlayerCallBack.prev();
                    break;
                case R.id.musicView_next:
                    musicPlayerCallBack.next();
                    break;
                case R.id.musicView_src:
                    musicPlayerCallBack.onShowMusicSrc();
                    break;
                case R.id.musicView_dl:
                    musicPlayerCallBack.onDownload();
                    break;
                case R.id.musicView_cmt:
                    musicPlayerCallBack.onComment();
                    break;
            }
        }
    }

    public static class MusicPlayerCallBackAdapter implements MusicPlayerCallBack {
        @Override
        public void onComment() {

        }

        @Override
        public void onDownload() {

        }

        @Override
        public void start() {

        }

        @Override
        public void pause() {

        }

        @Override
        public void next() {

        }

        @Override
        public void prev() {

        }

        @Override
        public void onShowMusicSrc() {

        }

        @Override
        public void onSliderChanged(int second) {

        }

        @Override
        public void onSliderFinished(int currentSecond) {

        }

        @Override
        public void onPlayTypeChange(int playType) {

        }
    }

    public interface MusicPlayerCallBack {

        void onComment();

        void onDownload();

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
         *
         * @param second
         */
        void onSliderChanged(int second);

        /**
         * 进度条滑动完成
         *
         * @param currentSecond
         */
        void onSliderFinished(int currentSecond);

        /**
         * LOOP,ONE,RANDOM
         *
         * @param playType
         */
        void onPlayTypeChange(int playType);

    }
}
