package edu.ptit.vhlee.minimusic.service;

import java.util.List;

import edu.ptit.vhlee.minimusic.ui.MediaPlayerListener;

public interface PlayerAdapter {
    void load();

    boolean isPlaying();

    void play();

    void pause();

    void seekTo(int position);

    void next();

    void previous();

    void stop();

    void reset();

    void release();

    void shuffle();

    void loop();
}
