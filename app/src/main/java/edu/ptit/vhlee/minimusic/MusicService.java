package edu.ptit.vhlee.minimusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

public class MusicService extends Service implements MusicPlayer {
    private IBinder mIBinder;

    public MusicService() {
        mIBinder = new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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
    public void playSong(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) mediaPlayer.start();
    }

    @Override
    public void pauseSong(MediaPlayer mediaPlayer) {
        if (!mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    @Override
    public void stopSong(MediaPlayer mediaPlayer) {

    }

    @Override
    public void loopSong(MediaPlayer mediaPlayer) {

    }

    @Override
    public void nextSong(MediaPlayer mediaPlayer) {

    }

    @Override
    public void previousSong(MediaPlayer mediaPlayer) {

    }

    @Override
    public void loopPlaylist(List<MediaPlayer> mediaPlayers) {

    }

    @Override
    public void shufflePlaylist(List<MediaPlayer> mediaPlayers) {

    }

    public class MusicBinder extends Binder {
        MusicService getMusicService() {
            return MusicService.this;
        }
    }

    public static Intent getServiceIntent(Context context) {
        Intent intent = new Intent(context, MusicService.class);
        return intent;
    }
}
