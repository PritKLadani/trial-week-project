package com.felipecsl.asymmetricgridview.app;

class VideoItem {

    private String videoLink;
    private int startAt;

    public VideoItem(String videoLink) {
        this.videoLink = videoLink;
        startAt = 0;
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
}
