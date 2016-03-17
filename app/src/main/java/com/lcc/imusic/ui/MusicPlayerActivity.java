package com.lcc.imusic.ui;

import android.os.Bundle;

import com.lcc.imusic.R;
import com.lcc.imusic.base.BaseActivity;
import com.lcc.imusic.musicplayer.MusicPlayerView;

import butterknife.Bind;

public class MusicPlayerActivity extends BaseActivity {
    @Bind(R.id.musicPlayer)
    MusicPlayerView musicPlayerView;

    private MusicPlayerView.MusicItem musicItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicPlayerView.setData(musicItem = (MusicPlayerView.MusicItem) getIntent().getSerializableExtra("music"));
        musicPlayerView.play();
        setTitle(musicItem.title);
        toolbar.setSubtitle(musicItem.artist);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }
}
