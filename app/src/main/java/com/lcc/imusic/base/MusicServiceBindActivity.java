package com.lcc.imusic.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.service.MusicPlayService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public abstract class MusicServiceBindActivity extends BaseActivity {

    protected MusicPlayService.MusicServiceBind musicServiceBind;
    private ServiceConnection serviceConnection;
    private List<OnBindMusicServiceListener> onBindMusicServiceListeners;

    private boolean isBind = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (shouldBind())
            bindMusicService();
    }

    public boolean isBind() {
        return isBind;
    }

    /************************
     * Music service
     **********************************/

    private void bindMusicService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        serviceConnection = new MusicServiceConnection();
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    protected void onBind(MusicPlayService.MusicServiceBind musicServiceBind) {
    }

    protected void unBind(MusicPlayService.MusicServiceBind musicServiceBind) {
    }

    public void addOnBindMusicServiceListener(@NonNull OnBindMusicServiceListener listener) {
        if (onBindMusicServiceListeners == null)
            onBindMusicServiceListeners = new ArrayList<>();
        onBindMusicServiceListeners.add(listener);
    }

    protected boolean shouldBind() {
        return true;
    }

    /************************
     * Music service
     **********************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unBind(musicServiceBind);
    }

    protected class MusicServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicServiceBind = (MusicPlayService.MusicServiceBind) service;
            onBind(musicServiceBind);
            isBind = true;
            if (onBindMusicServiceListeners != null) {
                for (OnBindMusicServiceListener listener : onBindMusicServiceListeners) {
                    listener.onBind(musicServiceBind);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
