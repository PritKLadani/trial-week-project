package com.felipecsl.asymmetricgridview.app;

import android.graphics.Bitmap;

class VideoItem {

    private String videoLink;
    private int startAt;
    private Bitmap thumbnail;

    public VideoItem(String videoLink) {
        this.videoLink = videoLink;
        startAt = 0;
        thumbnail = null;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
