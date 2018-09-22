package edu.ptit.vhlee.minimusic.ui;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.ptit.vhlee.minimusic.Constants;
import edu.ptit.vhlee.minimusic.R;
import edu.ptit.vhlee.minimusic.service.MusicService;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, ServiceConnection {
    private ServiceConnection mConnection;
    private MusicService mMusicService;
    private boolean mIsBoundService;
    private ImageView mShuffle;
    private ImageView mPrevious;
    private ImageView mPlay;
    private ImageView mNext;
    private ImageView mLoop;
    private TextView mCurrentTime;
    private TextView mDurationTime;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(MusicService.getServiceIntent(this), mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBoundService) {
            unbindService(mConnection);
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
        mMusicService.setMusicListener(new MusicListener());
        mIsBoundService = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mIsBoundService = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_previous:
                break;
            case R.id.image_play:
                    mMusicService.play();
                break;
            case R.id.image_next:
                break;
        }
    }

    private void initUI() {
        mShuffle = findViewById(R.id.image_shuffle);
        mPrevious = findViewById(R.id.image_previous);
        mPlay = findViewById(R.id.image_play);
        mNext = findViewById(R.id.image_next);
        mLoop = findViewById(R.id.image_loop);
        mCurrentTime = findViewById(R.id.text_current_time);
        mDurationTime = findViewById(R.id.text_duration_time);
        mSeekBar = findViewById(R.id.seek_bar_playing);
        setListener();
    }

    public void setListener() {
        mConnection = this;
        mShuffle.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mLoop.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
    }

    public String convertTime(int milis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT_PATTERN);
        return dateFormat.format(milis);
    }

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int position, boolean isFromUser) {
            if (isFromUser) {
                mMusicService.seekTo(position);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class MusicListener implements MediaPlayerListener {

        @Override
        public void onDurationChanged(int duration) {
            mDurationTime.setText(convertTime(duration));
            mSeekBar.setMax(duration);
        }

        @Override
        public void onPositionChanged(int position) {
            mCurrentTime.setText(convertTime(position));
            mSeekBar.setProgress(position);
        }

        @Override
        public void onStateChanged(int state) {
            switch (state) {

                case State.COMPLETED:
                    break;
                case State.INVALID:
                    break;
                case State.PAUSED:
                    mPlay.setImageResource(R.drawable.ic_play_white);
                    break;
                case State.PLAYING:
                    mPlay.setImageResource(R.drawable.ic_pause_white);
                    break;
                case State.RESET:
                    break;
            }
        }

        @Override
        public void onPlaybackCompleted() {

        }
    }
}
