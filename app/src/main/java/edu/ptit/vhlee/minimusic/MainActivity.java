package edu.ptit.vhlee.minimusic;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, ServiceConnection {
    private ServiceConnection mServiceConn;
    private MusicService mMusicService;
    private boolean mIsBoundService;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(MusicService.getServiceIntent(this), mServiceConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBoundService) {
            unbindService(mServiceConn);
            mIsBoundService = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) iBinder;
        mMusicService = musicBinder.getMusicService();
        mIsBoundService = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mIsBoundService = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_play:
                controlPlayButton((ImageView) view);
                break;
        }
    }

    private void initViews() {
        final int[] idButtons = {R.id.image_shuffle, R.id.image_previous, R.id.image_play,
                R.id.image_next, R.id.image_loop};
        SeekBar seekBar = findViewById(R.id.seek_bar_playing);
        TextView textDuration = findViewById(R.id.text_total_time);
        mMediaPlayer = MediaPlayer.create(this, R.raw.sample_song);
        mIsBoundService = false;
        mMusicService = null;
        mServiceConn = this;
        seekBar.setMax(mMediaPlayer.getDuration());
        textDuration.setText(convertTime(mMediaPlayer.getDuration()));
        setListener(idButtons, seekBar);
        syncSeekBar(seekBar);
    }

    public void setListener(int[] idButtons, SeekBar seekBar) {
        for (int id : idButtons) {
            ImageView function = findViewById(id);
            function.setOnClickListener(this);
        }
        seekBar.setOnSeekBarChangeListener(new SeekbarListener());
    }

    public void controlPlayButton(ImageView playimage) {
        if (mMediaPlayer.isPlaying()) {
            mMusicService.pauseSong(mMediaPlayer);
            playimage.setImageResource(R.drawable.ic_play_white);
        } else {
            mMusicService.playSong(mMediaPlayer);
            playimage.setImageResource(R.drawable.ic_pause_white);
        }
    }

    private void syncSeekBar(final SeekBar seekBar) {
        final Handler handler = new Handler();
        final TextView textCurrentTime = findViewById(R.id.text_playing_time);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    int mCurrentTime = mMediaPlayer.getCurrentPosition();
                    textCurrentTime.setText(convertTime(mMediaPlayer.getCurrentPosition()));
                    seekBar.setProgress(mCurrentTime);
                }
                handler.postDelayed(this, Constants.TIME_DELAY);
            }
        });
    }

    public String convertTime(int milis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT_PATTERN);
        return dateFormat.format(milis);
    }

    private class SeekbarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int position, boolean isFromUser) {
            if (mMediaPlayer != null && isFromUser) {
                mMediaPlayer.seekTo(position);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
