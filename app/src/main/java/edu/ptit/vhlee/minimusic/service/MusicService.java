package edu.ptit.vhlee.minimusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import edu.ptit.vhlee.minimusic.Constants;
import edu.ptit.vhlee.minimusic.R;
import edu.ptit.vhlee.minimusic.data.model.Track;
import edu.ptit.vhlee.minimusic.ui.MainActivity;
import edu.ptit.vhlee.minimusic.ui.MediaPlayerListener;

public class MusicService extends Service implements MediaPlayerListener {
    public static final int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_MAIN_CODE = 10;
    private IBinder mIBinder;
    private PlayerHolder mHolder;
    private RemoteViews mRemoteViews;
    private Notification mNotification;
    private MediaPlayerListener mPlayerListener;
    private NotificationManager mNotificationManager;

    public MusicService() {
        mIBinder = new MusicBinder();
        mHolder = new PlayerHolder(this);
    }

    @Override
    public void onCreate() {
        mPlayerListener = this;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDurationChanged(int duration) {

    }

    @Override
    public void onPositionChanged(int position) {

    }

    @Override
    public void onStateChanged(int state) {
        switch (state) {

            case State.COMPLETED:
                break;
            case State.LOOP:
                break;
            case State.NO_LOOP:
                break;
            case State.PAUSED:
                mRemoteViews.setImageViewResource(R.id.image_notify_play, R.drawable.ic_play_white);
                mNotification.contentView = mRemoteViews;
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                break;
            case State.PLAYING:
                mRemoteViews.setImageViewResource(R.id.image_notify_play, R.drawable.ic_pause_white);
                mNotification.contentView = mRemoteViews;
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                break;
        }
    }

    @Override
    public void onPlaybackCompleted() {

    }

    @Override
    public void onTrackChange(Track track) {
        mRemoteViews.setTextViewText(R.id.text_notify_title, track.getName());
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    public void createNotification() {
        Intent intent = MainActivity.getMainActivityIntent(this);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, NOTIFICATION_MAIN_CODE, intent, 0);
        mNotification = initNotification(pendingIntent);
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    private PendingIntent initPendingIntent(String action) {
        Intent intent = getServiceIntent(this);
        intent.setAction(action);
        PendingIntent pendingIntent =
                PendingIntent.getService(this, NOTIFICATION_MAIN_CODE, intent, 0);
        return pendingIntent;
    }

    private void initRemoteViews() {
        PendingIntent previousIntent =
                initPendingIntent(Constants.ACTION_PREVIOUS);
        PendingIntent playIntent =
                initPendingIntent(Constants.ACTION_PLAY);
        PendingIntent nextIntent =
                initPendingIntent(Constants.ACTION_NEXT);
        mRemoteViews =
                new RemoteViews(getPackageName(), R.layout.notification_custom_layout);
        mRemoteViews.setImageViewResource(R.id.image_notify_photo, R.drawable.default_photo_song);
        mRemoteViews.setTextViewText(R.id.text_notify_title, getString(R.string.notify_no_song));
        mRemoteViews.setImageViewResource(R.id.image_notify_previous, R.drawable.ic_previous_white);
        mRemoteViews.setImageViewResource(R.id.image_notify_play, R.drawable.ic_play_white);
        mRemoteViews.setImageViewResource(R.id.image_notify_next, R.drawable.ic_next_white);
        mRemoteViews.setOnClickPendingIntent(R.id.image_notify_next, nextIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.image_notify_play, playIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.image_notify_previous, previousIntent);
    }

    private Notification initNotification(PendingIntent pendingIntent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.default_photo_song);
        builder.setContent(mRemoteViews);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        switch (intent.getAction()) {
            case Constants.ACTION_NEXT:
                next();
                mPlayerListener.onTrackChange(mHolder.getTrack());
                break;
            case Constants.ACTION_PLAY:
                play();
                break;
            case Constants.ACTION_PREVIOUS:
                mPlayerListener.onTrackChange(mHolder.getTrack());
                previous();
                break;
        }
    }

    public void initPlayList(List<Track> tracks) {
        initRemoteViews();
        mHolder.initList(tracks);
    }

    public void play() {
        createNotification();
        if (!mHolder.isPlaying()) {
            mHolder.play();
        }
        else {
            mHolder.pause();
        }
    }

    public void seekTo(int position) {
        mHolder.seekTo(position);
    }

    public void next() {
        mHolder.next();
    }

    public void previous() {
        mHolder.previous();
    }

    public void setLoop() {
        mHolder.loop();
    }

    public void setMusicListener(MediaPlayerListener listener) {
        mHolder.setPlayerListener(listener);
    }

    public class MusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    public static Intent getServiceIntent(Context context) {
        return new Intent(context, MusicService.class);
    }
}
