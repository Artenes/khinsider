package com.artenesnogueira.khinsider.player;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MusicPlayerService extends Service {

    private static final String USER_AGENT = "Khinsider/0.0.1";

    private final IBinder mBinder = new MusicPlayerBinder();
    private SimpleExoPlayer mPlayer;

    boolean hasStarted = false;
    boolean isPlaying = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = ExoPlayerFactory.newSimpleInstance(this);
        mPlayer.setPlayWhenReady(true);
        mPlayer.seekTo(0, 0);
    }

    public void startMusic(String url) {
        Uri uri = Uri.parse(url);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(USER_AGENT)).createMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
        hasStarted = true;
        isPlaying = true;
    }

    public boolean hasMusicStarted() {
        return hasStarted;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        mPlayer.setPlayWhenReady(true);
        isPlaying = true;
    }

    public void pause() {
        mPlayer.setPlayWhenReady(false);
        isPlaying = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        mPlayer = null;
    }

    public class MusicPlayerBinder extends Binder {

        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }

    }

}
