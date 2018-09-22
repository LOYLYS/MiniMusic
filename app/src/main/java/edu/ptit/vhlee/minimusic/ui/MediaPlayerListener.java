package edu.ptit.vhlee.minimusic.ui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edu.ptit.vhlee.minimusic.data.model.Track;

public interface MediaPlayerListener {
    @IntDef({State.LOOP, State.PLAYING, State.PAUSED, State.NO_LOOP, State.COMPLETED})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
        int PLAYING = 0;
        int PAUSED = 1;
        int LOOP = 2;
        int NO_LOOP = 3;
        int COMPLETED = 4;
    }

    void onDurationChanged(int duration);

    void onPositionChanged(int position);

    void onStateChanged(@State int state);

    void onPlaybackCompleted();

    void onTrackChange(Track track);
}
