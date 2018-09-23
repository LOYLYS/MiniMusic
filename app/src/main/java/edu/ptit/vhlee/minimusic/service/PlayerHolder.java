package edu.ptit.vhlee.minimusic.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import edu.ptit.vhlee.minimusic.Constants;
import edu.ptit.vhlee.minimusic.ui.MediaPlayerListener;

public class PlayerHolder implements PlayerAdapter {
    private Context mContext;
    private MediaPlayer mPlayer;
    private MediaPlayerListener mPlayerListener;

    public PlayerHolder(Context context) {
        mContext = context;
    }

    @Override
    public void load(int resourceId) {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(mContext, resourceId);
        }

        if (mPlayerListener != null) {
            mPlayerListener.onStateChanged(MediaPlayerListener.State.PLAYING);
        }
    }

    @Override
    public boolean isPlaying() {
        if (mPlayer != null) return mPlayer.isPlaying();
        return false;
    }

    @Override
    public void play() {
        if (mPlayer != null && !mPlayer.isPlaying()) mPlayer.start();
        if (mPlayerListener != null) {
            mPlayerListener.onStateChanged(MediaPlayerListener.State.PLAYING);
        }
        startSyncSeekBar();
    }

    @Override
    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) mPlayer.pause();
        if (mPlayerListener != null) {
            mPlayerListener.onStateChanged(MediaPlayerListener.State.PAUSED);
        }
    }

    @Override
    public void seekTo(int position) {
        if (mPlayer !=null) {
            mPlayer.seekTo(position);
        }
        if (mPlayerListener != null) {
            mPlayerListener.onPositionChanged(position);
        }
    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void release() {

    }

    @Override
    public void shuffle() {

    }

    @Override
    public void loop(int loopType) {

    }

    public void setPlayerListener(MediaPlayerListener listener) {
        mPlayerListener = listener;
    }

    private void startSyncSeekBar() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayerListener.onDurationChanged(mPlayer.getDuration());
                    mPlayerListener.onPositionChanged(mPlayer.getCurrentPosition());
                }
                handler.postDelayed(this, Constants.TIME_DELAY);
            }
        });
    }
}
