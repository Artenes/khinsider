package com.artenesnogueira.khinsider.player;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.artenesnogueira.khinsider.api.model.Format;
import com.artenesnogueira.khinsider.api.model.Song;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MusicPlayerService extends Service {

    private static final String USER_AGENT = "Khinsider/0.0.1";

    private final IBinder mBinder = new MusicPlayerBinder();
    private SimpleExoPlayer mPlayer;

    private boolean mHasStarted = false;
    private boolean mIsPlaying = false;
    private Song mCurrentSong = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = ExoPlayerFactory.newSimpleInstance(this);
    }

    public void startMusic(Song song) {

        if (mCurrentSong == song) {
            playOrPause();
            return;
        }

        if (mCurrentSong != null) {
            mCurrentSong.setPlaying(false);
        }

        Uri uri = Uri.parse(song.getFiles().get(Format.MP3).getUrl());
        MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(USER_AGENT)).createMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
        mHasStarted = true;
        mIsPlaying = true;
        mCurrentSong = song;
        play();
        mPlayer.seekTo(0, 0);
    }

    public boolean isPlaying(Song song) {
        return mCurrentSong == song;
    }

    public boolean hasMusicStarted() {
        return mHasStarted;
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void playOrPause() {
        if (mIsPlaying) {
            pause();
        } else {
            play();
        }
    }

    public void play() {
        mPlayer.setPlayWhenReady(true);
        mIsPlaying = true;
        mCurrentSong.setPlaying(true);
    }

    public void pause() {
        mPlayer.setPlayWhenReady(false);
        mIsPlaying = false;
        mCurrentSong.setPlaying(false);
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
