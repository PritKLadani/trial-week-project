package com.felipecsl.asymmetricgridview.app;

import android.widget.MediaController;

import com.google.android.exoplayer2.ExoPlayer;

public class PlayerControl implements MediaController.MediaPlayerControl {

    private final ExoPlayer exoPlayer;

    public PlayerControl(ExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBufferPercentage() {
        return exoPlayer.getBufferedPercentage();
    }

    @Override
    public int getCurrentPosition() {
        return exoPlayer.getDuration() == -1 ? 0
                : (int) exoPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return exoPlayer.getDuration() == -1 ? 0
                : (int) exoPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return exoPlayer.getPlayWhenReady();
    }

    @Override
    public void start() {
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void seekTo(int timeMillis) {
        long seekPosition = exoPlayer.getDuration() == -1? 0
                : Math.min(Math.max(0, timeMillis), getDuration());
        exoPlayer.seekTo(seekPosition);
    }

}
