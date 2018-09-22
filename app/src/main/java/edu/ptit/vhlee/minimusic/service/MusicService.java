package edu.ptit.vhlee.minimusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import edu.ptit.vhlee.minimusic.data.model.Track;
import edu.ptit.vhlee.minimusic.ui.MediaPlayerListener;

public class MusicService extends Service {
    private IBinder mIBinder;
    private PlayerHolder mHolder;

    public MusicService() {
        mIBinder = new MusicBinder();
        mHolder = new PlayerHolder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

    public void play() {
        if (!mHolder.isPlaying()) mHolder.play();
        else mHolder.pause();
    }

    public void initPlayList(List<Track> tracks) {
        mHolder.initList(tracks);
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
        Intent intent = new Intent(context, MusicService.class);
        return intent;
    }
}
