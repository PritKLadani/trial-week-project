package com.felipecsl.asymmetricgridview.app;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class VideoViewActivity extends AppCompatActivity {

    RecyclerView recyclerViewVideo;
    LinearLayoutManager llm;
    int positionOfItemPlaying = -1;
    private boolean isSmoothScrolling = false;
    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewVideo = findViewById(R.id.video_view_recycler);

        final List<VideoItem> videoLinks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            videoLinks.add(new VideoItem("https://sample-videos.com/video123/mp4/240/big_buck_bunny_240p_30mb.mp4"));
        }

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(VideoViewActivity.this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        final VideoViewRecyclerAdapter adapter = new VideoViewRecyclerAdapter(this, videoLinks, player);

        recyclerViewVideo.setAdapter(adapter);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewVideo.setLayoutManager(llm);

        adapter.getItemId(llm.findFirstVisibleItemPosition());

        llm.findViewByPosition(llm.findFirstVisibleItemPosition());

        recyclerViewVideo.findViewHolderForAdapterPosition(llm.findFirstVisibleItemPosition());

        recyclerViewVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int position = llm.findFirstCompletelyVisibleItemPosition();

                //if (dy > 0) {
                if (position != -1 && position != positionOfItemPlaying && position < adapter.getItemCount()) {
                    Log.d("final", "findFirstCompletelyVisibleItemPosition: " + position);
                    positionOfItemPlaying = position;
                    Log.d("TAG", "playing at: " + position + " stopping at: " + (position + 1));

                    if (position - 1 >= 0) {
                        VideoViewRecyclerAdapter.MyVideoHolder previousHolder =
                                ((VideoViewRecyclerAdapter.MyVideoHolder) recyclerView.findViewHolderForAdapterPosition(position - 1));
                        if (previousHolder != null && previousHolder.isPlaying) {
                            Log.d("final", "previous: time = " + player.getContentPosition() + " position = " + (position - 1));
                            videoLinks.get(position - 1).setStartAt((int) player.getCurrentPosition());
                            previousHolder.updateVisibility(false);
                            /*Bitmap bitmap = null;
                            try {
                                MyRunnable myRunnable = new MyRunnable(videoLinks.get(position - 1).getVideoLink(), videoLinks.get(position - 1).getStartAt(), previousHolder.thumbnail);
                                myRunnable.run();
//                                bitmap = retriveVideoFrameFromVideo(videoLinks.get(position - 1).getVideoLink(), videoLinks.get(position - 1).getStartAt());
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                            if (bitmap != null) {
                                Log.d("final", "thumbnail set");
                                bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
                                previousHolder.thumbnail.setImageBitmap(bitmap);
                            }*/

                            /*BitmapPool bitmapPool = Glide.get(getApplicationContext()).getBitmapPool();
                            VideoBitmapDecoder videoBitmapDecoder = new VideoBitmapDecoder(videoLinks.get(position - 1).getStartAt() * 1000);
                            FileDescriptorBitmapDecoder fileDescriptorBitmapDecoder = new FileDescriptorBitmapDecoder(videoBitmapDecoder, bitmapPool, DecodeFormat.PREFER_ARGB_8888);
                            Glide.with(getApplicationContext())
                                    .load(videoLinks.get(position - 1).getVideoLink())
                                    .asBitmap()
                                    .videoDecoder(fileDescriptorBitmapDecoder)
                                    .into(previousHolder.thumbnail);*/
                            player.setPlayWhenReady(false);
                            previousHolder.playerView.setPlayer(null);
                            previousHolder.isPlaying = false;
                        }
                    }

                    if (position + 1 < adapter.getItemCount() - 1) {
                        VideoViewRecyclerAdapter.MyVideoHolder nextHolder =
                                ((VideoViewRecyclerAdapter.MyVideoHolder) recyclerView.findViewHolderForAdapterPosition(position + 1));
                        if (nextHolder != null && nextHolder.isPlaying) {
                            Log.d("final", "next: time = " + player.getContentPosition() + " position = " + (position + 1));
                            videoLinks.get(position + 1).setStartAt((int) player.getCurrentPosition());
                            nextHolder.updateVisibility(false);
                            /*Bitmap bitmap = null;
                            try {
                                bitmap = retriveVideoFrameFromVideo(videoLinks.get(position + 1).getVideoLink(), videoLinks.get(position + 1).getStartAt());
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                            if (bitmap != null) {
                                bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
                                nextHolder.thumbnail.setImageBitmap(bitmap);
                            }*/
                            player.setPlayWhenReady(false);
                            nextHolder.playerView.setPlayer(null);
                            nextHolder.isPlaying = false;
                        }
                    }

                    VideoViewRecyclerAdapter.MyVideoHolder holder =
                            ((VideoViewRecyclerAdapter.MyVideoHolder) recyclerView.findViewHolderForAdapterPosition(position));
                    //holder.initializePlayer(videoLinks.get(position).getVideoLink(), videoLinks.get(position).getStartAt());
                    //holder.playerView.setForeground(null);
                    Log.d("final", "current: time = " + videoLinks.get(position).getStartAt());
                    holder.playerView.setPlayer(player);
                    holder.updateVisibility(true);
                    holder.startPlaying(videoLinks.get(position).getVideoLink(), videoLinks.get(position).getStartAt());
                    //player.setPlayWhenReady(true);
                    holder.isPlaying = true;

                    //nextHolder.player.release();
                    //holder.playerView.setForeground(ContextCompat.getDrawable(VideoViewActivity.this, R.drawable.video_overlay));
                }


                //Infinite scrolling
                if (position > adapter.getItemCount() - 3 && dy > 0) {
                    Log.d("Temp_log_infinite", "infinite scroll enabled at: " + position);
                    final List<VideoItem> temp = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        temp.add(new VideoItem("https://sample-videos.com/video123/mp4/240/big_buck_bunny_240p_30mb.mp4"));
                    }
                    int oldSize = adapter.getItemCount();
                    videoLinks.addAll(temp);
                    adapter.notifyItemRangeInserted(oldSize, 10);
                    Log.d("Temp_log_infinite", "newSize: " + videoLinks.size() + " " + adapter.getItemCount());
                }

                //Smooth Scrolling
                /*if(!isSmoothScrolling) {
                    isSmoothScrolling = true;
                    if (dy > 0 && position < adapter.getItemCount() - 2) {
                        RecyclerView.SmoothScroller smoothScroller = new
                                LinearSmoothScroller(VideoViewActivity.this) {
                                    @Override
                                    protected int getVerticalSnapPreference() {
                                        return LinearSmoothScroller.SNAP_TO_START;
                                    }
                                };
                        smoothScroller.setTargetPosition(position + 1);
                        llm.startSmoothScroll(smoothScroller);

                    } else if (dy < 0 && position > 1) {
                        RecyclerView.SmoothScroller smoothScroller = new
                                LinearSmoothScroller(VideoViewActivity.this) {
                                    @Override
                                    protected int getVerticalSnapPreference() {
                                        return LinearSmoothScroller.SNAP_TO_START;
                                    }
                                };
                        smoothScroller.setTargetPosition(position - 1);
                        llm.startSmoothScroll(smoothScroller);
                    }
                    isSmoothScrolling = false;
                }*/

                /*int firstVisibleItem = llm.findFirstVisibleItemPosition();
                if (firstVisibleItem != positionOfItemPlaying) {
                    ((VideoViewRecyclerAdapter.MyVideoHolder) recyclerView.findViewHolderForAdapterPosition(firstVisibleItem))
                            .player.setPlayWhenReady(true);
                    ((VideoViewRecyclerAdapter.MyVideoHolder) recyclerView.findViewHolderForAdapterPosition(firstVisibleItem-1))
                            .player.setPlayWhenReady(false);
                    positionOfItemPlaying = firstVisibleItem;
                }*/
            }
        });
    }

    class MyRunnable implements Runnable {

        String url;
        int time;
        ImageView imageView;

        public MyRunnable(String url, int time, ImageView imageView) {
            this.url = url;
            this.time = time;
            this.imageView = imageView;
        }

        @Override
        public void run()  {
            Bitmap result;
            try {
                result = retriveVideoFrameFromVideo(url, time);
                imageView.setImageBitmap(result);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath, long time) throws Throwable {
        Bitmap bitmap = null;
        Log.d("final", "thumbnail: time = " + time + " link = " + videoPath);
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(time * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(VideoViewActivity.this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
    }

    public Player.EventListener playerEventListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case ExoPlayer.STATE_ENDED:
                    /*RecyclerView recyclerView = ((Activity) VideoViewActivity.this).findViewById(R.id.video_view_recycler);
                    RecyclerView.LayoutManager llm = recyclerView.getLayoutManager();
                    RecyclerView.SmoothScroller smoothScroller = new
                            LinearSmoothScroller(VideoViewActivity.this) {
                                @Override
                                protected int getVerticalSnapPreference() {
                                    return LinearSmoothScroller.SNAP_TO_START;
                                }
                            };
                    smoothScroller.setTargetPosition(position + 1);
                    llm.startSmoothScroll(smoothScroller);*/
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };
}
