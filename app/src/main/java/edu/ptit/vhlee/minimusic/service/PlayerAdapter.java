package edu.ptit.vhlee.minimusic.service;

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
