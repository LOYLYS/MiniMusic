package edu.ptit.vhlee.minimusic.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.ptit.vhlee.minimusic.Constants;
import edu.ptit.vhlee.minimusic.R;
import edu.ptit.vhlee.minimusic.data.local.MusicStorage;
import edu.ptit.vhlee.minimusic.data.model.Track;
import edu.ptit.vhlee.minimusic.service.MusicService;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, ServiceConnection {
    private static final int REQUEST_PERMISSION = 1;
    private ServiceConnection mConnection;
    private MusicService mMusicService;
    private boolean mIsBoundService;
    private boolean mPermission;
    private ImageView mShuffle;
    private ImageView mPrevious;
    private ImageView mPlay;
    private ImageView mNext;
    private ImageView mLoop;
    private TextView mTitle;
    private TextView mCurrentTime;
    private TextView mDurationTime;
    private SeekBar mSeekBar;
    private List<Track> mTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMusicService();
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
        checkPermission();
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
                mMusicService.previous();
                break;
            case R.id.image_play:
                mMusicService.play();
                break;
            case R.id.image_next:
                mMusicService.next();
                break;
            case R.id.image_loop:
                mMusicService.setLoop();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_DENIED) {
                    initData();
                } else checkPermission();
                break;
            default:
                break;
        }
    }

    private void checkPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, permissions[0])
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_PERMISSION);
        } else initData();
    }

    public void startMusicService() {
        startService(MusicService.getServiceIntent(this));
        bindService(MusicService.getServiceIntent(this), mConnection, BIND_AUTO_CREATE);
    }

    private void initUI() {
        mPermission = false;
        mTracks = new ArrayList<>();
        mShuffle = findViewById(R.id.image_shuffle);
        mPrevious = findViewById(R.id.image_previous);
        mPlay = findViewById(R.id.image_play);
        mNext = findViewById(R.id.image_next);
        mLoop = findViewById(R.id.image_loop);
        mTitle = findViewById(R.id.text_title);
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

    public void initData() {
        mTracks = MusicStorage.loadTracks(Constants.DEFAULT_PATH);
        if (mTracks != null) mMusicService.initPlayList(mTracks);
    }

    public String convertTime(int milliseconds) {
        SimpleDateFormat timeFormat =
                new SimpleDateFormat(Constants.TIME_FORMAT_PATTERN, Locale.US);
        return timeFormat.format(milliseconds);
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
                case State.LOOP:
                    mLoop.setImageResource(R.drawable.ic_loop_white);
                    break;
                case State.NO_LOOP:
                    mLoop.setImageResource(R.drawable.ic_loop_one_white);
                    break;
                case State.PAUSED:
                    mPlay.setImageResource(R.drawable.ic_play_white);
                    break;
                case State.PLAYING:
                    mPlay.setImageResource(R.drawable.ic_pause_white);
                    break;
            }
        }

        @Override
        public void onPlaybackCompleted() {

        }

        @Override
        public void onTrackChange(Track track) {
            mTitle.setText(track.getName());
        }
    }
}
