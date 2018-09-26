package edu.ptit.vhlee.minimusic.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.vhlee.minimusic.Constants;
import edu.ptit.vhlee.minimusic.data.local.MusicStorage;
import edu.ptit.vhlee.minimusic.data.model.Track;
import edu.ptit.vhlee.minimusic.ui.MediaPlayerListener;

public class PlayerHolder implements PlayerAdapter,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private Context mContext;
    private int mTrackCurrentPosition;
    private MediaPlayer mPlayer;
    private List<Track> mTracks;
    private MediaPlayerListener mPlayerListener;

    public PlayerHolder(Context context) {
        mContext = context;
    }

    public void initList(List<Track> tracks) {
        mTracks = new ArrayList<>();
        mTracks = tracks;
        mTrackCurrentPosition = 0;
        load();
        mPlayerListener.onStateChanged(MediaPlayerListener.State.PAUSED);
        mPlayerListener.onTrackChange(tracks.get(mTrackCurrentPosition));
    }

    @Override
    public void load() {
        Track track = mTracks.get(mTrackCurrentPosition);
        mPlayer = MediaPlayer.create(mContext, Uri.parse(track.getPath()));
        if (mPlayerListener != null) {
            mPlayer.setOnCompletionListener(this);
            mPlayerListener.onStateChanged(MediaPlayerListener.State.PLAYING);
            mPlayerListener.onTrackChange(track);
        }
    }

    @Override
    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    @Override
    public void play() {
        if (mPlayer == null) load();
        else if (!mPlayer.isPlaying()) mPlayer.start();
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
        if (mPlayer != null) {
            mPlayer.seekTo(position);
        }
        if (mPlayerListener != null) {
            mPlayerListener.onPositionChanged(position);
        }
    }

    @Override
    public void next() {
        reset();
        mTrackCurrentPosition++;
        if (mTrackCurrentPosition == mTracks.size()) mTrackCurrentPosition = 0;
        load();
        play();
    }

    @Override
    public void previous() {
        reset();
        mTrackCurrentPosition--;
        if (mTrackCurrentPosition < 0) mTrackCurrentPosition = mTracks.size() - 1;
        load();
        play();
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void reset() {
        mPlayer.reset();
    }

    @Override
    public void release() {
        mPlayer.release();
    }

    @Override
    public void shuffle() {

    }

    @Override
    public void loop() {
        if (mPlayer.isLooping()) {
            mPlayer.setLooping(false);
            mPlayerListener.onStateChanged(MediaPlayerListener.State.LOOP);
        } else {
            mPlayer.setLooping(true);
            mPlayerListener.onStateChanged(MediaPlayerListener.State.NO_LOOP);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mPlayerListener.onTrackChange(mTracks.get(mTrackCurrentPosition));
    }

    public Track getTrack() {
        return mTracks.get(mTrackCurrentPosition);
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
