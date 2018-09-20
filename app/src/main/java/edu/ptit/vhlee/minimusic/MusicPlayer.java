package edu.ptit.vhlee.minimusic;

import android.media.MediaPlayer;

import java.util.List;

public interface MusicPlayer {
    void playSong(MediaPlayer mediaPlayer);

    void pauseSong(MediaPlayer mediaPlayer);

    void stopSong(MediaPlayer mediaPlayer);

    void loopSong(MediaPlayer mediaPlayer);

    void nextSong(List<MediaPlayer> mediaPlayers, int position);

    void previousSong(List<MediaPlayer> mediaPlayers, int position);

    void loopPlaylist(List<MediaPlayer> mediaPlayers);

    void shufflePlaylist(List<MediaPlayer> mediaPlayers);
}
