package com.lcc.imusic.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by lcc_luffy on 2016/3/20.
 */
public class MusicNotificationManager {

    public static final String ACTION_MUSIC_PLAY_OR_PAUSE = "com.lcc.music.play";
    public static final String ACTION_MUSIC_NEXT = "com.lcc.music.next";
    MusicPlayService musicPlayService;
    private boolean hasNotified = false;
    MusicControllerReceiver musicControllerReceiver;
    private Notification notification;
    Bitmap pauseBitmap;
    Bitmap playBitmap;
    private NotificationManagerCompat managerCompat;

    private RemoteViews remoteViews;

    private static final int NOTIFICATION_ID = 2946;

    private Context context;

    public MusicNotificationManager(@NonNull MusicPlayService musicPlayService) {
        this.musicPlayService = musicPlayService;

        context = musicPlayService.getApplicationContext();

        pauseBitmap = BitmapFactory.decodeResource(musicPlayService.getResources()
                , R.mipmap.playbar_btn_pause);
        playBitmap = BitmapFactory.decodeResource(musicPlayService.getResources()
                , R.mipmap.playbar_btn_play);
    }

    public void notifyNotification() {
        hasNotified = true;
        musicControllerReceiver = new MusicControllerReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MUSIC_NEXT);
        intentFilter.addAction(ACTION_MUSIC_PLAY_OR_PAUSE);

        musicPlayService.registerReceiver(musicControllerReceiver, intentFilter);

        remoteViews = new RemoteViews(musicPlayService.getPackageName(), R.layout.notification_play_panel);
        Intent play = new Intent(ACTION_MUSIC_PLAY_OR_PAUSE);
        PendingIntent playIntent = PendingIntent.getBroadcast(context, 0, play, 0);
        remoteViews.setOnClickPendingIntent(R.id.notification_play_or_pause, playIntent);


        Intent next = new Intent(ACTION_MUSIC_NEXT);
        PendingIntent nextIntent = PendingIntent.getBroadcast(context, 0, next, 0);
        remoteViews.setOnClickPendingIntent(R.id.notification_next, nextIntent);


        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context);

        builder.setOngoing(true);
        builder.setContent(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(context, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        managerCompat = NotificationManagerCompat.from(context);
        musicPlayService.startForeground(NOTIFICATION_ID, notification = builder.build());
    }

    public boolean hasNotified() {
        return hasNotified;
    }

    public void update(MusicItem musicItem) {
        remoteViews.setTextViewText(R.id.notification_title, musicItem.title);
        remoteViews.setTextViewText(R.id.notification_subtitle, musicItem.artist);
        remoteViews.setImageViewBitmap(R.id.notification_play_or_pause, pauseBitmap);
        managerCompat.notify(NOTIFICATION_ID, notification);
        /*Glide.with(MusicPlayService.this)
                    .load(item.cover)
                    .placeholder(R.mipmap.mycover)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            remoteViews.setImageViewBitmap(R.id.notification_cover, Common.drawable2Bitmap(resource));
                        }
                    });*/

    }

    public void pause() {
        if (!hasNotified)
            return;
        remoteViews.setImageViewBitmap(R.id.notification_play_or_pause, playBitmap);
        managerCompat.notify(NOTIFICATION_ID, notification);
    }

    public void onDestroy() {
        if (musicControllerReceiver != null)
            musicPlayService.unregisterReceiver(musicControllerReceiver);
        context = null;
        musicPlayService = null;
        remoteViews = null;
        managerCompat = null;
    }

    public class MusicControllerReceiver extends BroadcastReceiver {
        private boolean playing = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.i(action);
            if (ACTION_MUSIC_NEXT.equals(action)) {
                musicPlayService.nextMusic();
            } else if (ACTION_MUSIC_PLAY_OR_PAUSE.equals(action)) {
                if (playing) {
                    playing = false;

                    musicPlayService.pauseMusic();
                } else {
                    playing = true;
                    musicPlayService.startPlayOrResume();

                }
                if (playing) {
                    remoteViews.setImageViewBitmap(R.id.notification_play_or_pause, pauseBitmap);
                    managerCompat.notify(NOTIFICATION_ID, notification);
                }
            }
        }
    }
}
