package com.lcc.imusic.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.ui.setting.SettingActivity;
import com.lcc.imusic.ui.user.UserCenterActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class AccountDelegate {
    private Toolbar toolbar;
    private Activity activity;

    private AccountListener accountListener;

    protected Drawer drawer;
    protected AccountHeader header;
    protected ProfileDrawerItem profileDrawerItem;

    public AccountDelegate(@NonNull Activity activity, @NonNull Toolbar toolbar, @NonNull AccountListener listener) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.accountListener = listener;
    }

    public void init() {
        header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(profileDrawerItem = new ProfileDrawerItem().withEmail("username").withName("email@example.com"))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        activity.startActivity(new Intent(activity, UserCenterActivity.class));
                        drawer.closeDrawer();
                        return true;
                    }
                })
                .build();


        final PrimaryDrawerItem setting = new PrimaryDrawerItem()
                .withIcon(FontAwesome.Icon.faw_cogs)
                .withName("设置");
        drawer = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(activity)
                .withDisplayBelowStatusBar(false)
                .withDrawerItems(accountListener.onCreateMenuItem())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem == setting) {
                            activity.startActivity(new Intent(activity, SettingActivity.class));
                            drawer.closeDrawer();
                            return true;
                        }
                        return accountListener.onDrawerMenuSelected(view, position, drawerItem);
                    }
                })
                .withAccountHeader(header)
                .build();
        drawer.addStickyFooterItem(setting);
        drawer.getDrawerLayout().setFitsSystemWindows(true);
        drawer.getSlider().setFitsSystemWindows(true);
    }

    public void setUsername(String username) {
        profileDrawerItem.withName(username);
        header.updateProfile(profileDrawerItem);
    }

    public void setEmail(String email) {
        profileDrawerItem.withEmail(email);
        header.updateProfile(profileDrawerItem);
    }

    public void setAvatar(String url) {
        profileDrawerItem.withIcon(url);
        header.updateProfile(profileDrawerItem);
    }

    public void setAvatar(@DrawableRes int resId) {
        profileDrawerItem.withIcon(resId);
        header.updateProfile(profileDrawerItem);
    }

    public void setHeaderBackground(String url) {
        header.setHeaderBackground(new ImageHolder(url));
    }

    public void setHeaderBackground(@DrawableRes int resId) {
        header.setHeaderBackground(new ImageHolder(resId));
    }

    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        return false;
    }

    public void setAccountListener(AccountListener accountListener) {
        this.accountListener = accountListener;
    }

    public void destroy() {
        header.clear();
        header = null;
        profileDrawerItem = null;
        accountListener = null;
        activity = null;
        toolbar = null;
        drawer = null;
    }

    public interface AccountListener {
        boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem);

        @NonNull
        List<IDrawerItem> onCreateMenuItem();
    }
}
