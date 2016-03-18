package com.lcc.imusic.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.service.MusicPlayService;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/17.
 */
public abstract class AccountActivity extends BaseActivity{
    protected Drawer drawer;
    protected AccountHeader header;
    protected ProfileDrawerItem profileDrawerItem;

    protected MusicPlayService.MusicServiceBind musicServiceBind;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAccount();
        Intent intent = new Intent(this, MusicPlayService.class);
        serviceConnection = new MusicServiceConnection();
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    protected void onBind(MusicPlayService.MusicServiceBind musicServiceBind)
    {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void initAccount() {
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.user_info_bg)
                .addProfiles(profileDrawerItem = new ProfileDrawerItem().withEmail("username").withName("email@example.com"))
                .build();

        final PrimaryDrawerItem setting = new PrimaryDrawerItem()
                .withName("设置");
        drawer = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(this)
                .withDrawerItems(onCreateMenuItem())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return onDrawerMenuSelected(view,position,drawerItem);
                    }
                })
                .withFooterDivider(true)
                .withAccountHeader(header)
                .build();
        drawer.addStickyFooterItem(setting);
    }

    public void setUsername(String username)
    {
        profileDrawerItem.withName(username);
        header.updateProfile(profileDrawerItem);
    }

    public void setEmail(String email)
    {
        profileDrawerItem.withEmail(email);
        header.updateProfile(profileDrawerItem);
    }


    public void setAvatar(String url)
    {
        profileDrawerItem.withIcon(url);
        header.updateProfile(profileDrawerItem);
    }

    public void setAvatar(@DrawableRes int resId)
    {
        profileDrawerItem.withIcon(resId);
        header.updateProfile(profileDrawerItem);
    }
    public void setHeaderBackground(String url)
    {
        header.setHeaderBackground(new ImageHolder(url));
    }

    public void setHeaderBackground(@DrawableRes int resId)
    {
        header.setHeaderBackground(new ImageHolder(resId));
    }
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem)
    {
        return false;
    }

    @NonNull
    public List<IDrawerItem> onCreateMenuItem()
    {
        return new ArrayList<IDrawerItem>();
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
