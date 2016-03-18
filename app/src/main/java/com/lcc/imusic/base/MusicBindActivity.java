package com.lcc.imusic.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.lcc.imusic.service.MusicPlayService;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public abstract class MusicBindActivity extends BaseActivity {
    protected MusicPlayService.MusicServiceBind musicServiceBind;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MusicPlayService.class);
        serviceConnection = new MusicServiceConnection();
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    protected void onBind(MusicPlayService.MusicServiceBind musicServiceBind)
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    protected class MusicServiceConnection implements ServiceConnection
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicServiceBind = (MusicPlayService.MusicServiceBind) service;
            onBind(musicServiceBind);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
