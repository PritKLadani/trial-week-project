package com.felipecsl.asymmetricgridview.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;

public class VideoViewRecyclerAdapter extends RecyclerView.Adapter<VideoViewRecyclerAdapter.MyVideoHolder> {

    Context mContext;
    List<VideoItem> videoItems;
    ExoPlayer player;

    public VideoViewRecyclerAdapter(Context context, List<VideoItem> videoLinks, ExoPlayer player) {
        this.mContext = context;
        this.videoItems = videoLinks;
        this.player = player;
    }

    @Override
    public void onViewRecycled(@NonNull MyVideoHolder holder) {
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public MyVideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_video_item, viewGroup, false);
        return new MyVideoHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyVideoHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int position = holder.getLayoutPosition();
        Log.d("Temp_log", "detached: " + holder.getAdapterPosition() + " " + holder.getLayoutPosition());
        //holder.playerControl.pause();
        if(holder.isPlaying) {
            videoItems.get(position).setStartAt((int) player.getCurrentPosition());
            holder.updateVisibility(false);
            player.setPlayWhenReady(false);
            holder.playerView.setPlayer(null);
            holder.isPlaying = false;
            Log.d("Temp_log_infinite", "detached position: " + position + " time: " + holder.playerView.getPlayer().getCurrentPosition());
        }
        //holder.playerView.setForeground(ContextCompat.getDrawable(mContext, R.drawable.video_overlay));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyVideoHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        //Log.d("Temp_log", "attached: "+ holder.getAdapterPosition() + " " + holder.getLayoutPosition());
        //holder.initializePlayer(videoItems.get(position).getVideoLink(), videoItems.get(position).getStartAt());
        //holder.playerControl.seekTo(videoItems.get(position).getStartAt());
//        holder.playerControl.start();
//        Log.d("Temp_log", "attached position: "+ position+ " time: "+ videoItems.get(position).getStartAt());
    }

    @Override
    public void onBindViewHolder(@NonNull MyVideoHolder myVideoHolder, final int position) {

        if(position == 0) {
            myVideoHolder.playerView.setPlayer(player);
            myVideoHolder.startPlaying(videoItems.get(position).getVideoLink(), videoItems.get(position).getStartAt());
            myVideoHolder.isPlaying = true;
        }

        //myVideoHolder.initializePlayer(videoItems.get(position).getVideoLink(), videoItems.get(position).getStartAt());

        /*myVideoHolder.playerView.getPlayer().addListener(new Player.EventListener() {
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
                        RecyclerView recyclerView = ((Activity) mContext).findViewById(R.id.video_view_recycler);
                        RecyclerView.LayoutManager llm = recyclerView.getLayoutManager();
                        RecyclerView.SmoothScroller smoothScroller = new
                                LinearSmoothScroller(mContext) {
                                    @Override
                                    protected int getVerticalSnapPreference() {
                                        return LinearSmoothScroller.SNAP_TO_START;
                                    }
                                };
                        smoothScroller.setTargetPosition(position + 1);
                        llm.startSmoothScroll(smoothScroller);
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
        });*/
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public class MyVideoHolder extends RecyclerView.ViewHolder {

        PlayerView playerView;
        ImageView thumbnail;
        boolean isPlaying;
        //ExoPlayer player;

        public MyVideoHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.video_view_holder_exoplayer);
            thumbnail = itemView.findViewById(R.id.thumbnail);
//            playerView.setForeground(null);

            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

//            initializePlayer();
        }

        public void setPlayer(ExoPlayer player) {
            playerView.setPlayer(player);
        }

        public void startPlaying(String url, int startTime) {
            Uri uri = Uri.parse(url);
            MediaSource mediaSource = buildMediaSource(uri);
            ((ExoPlayer) playerView.getPlayer()).prepare(mediaSource, true, false);
            playerView.getPlayer().setRepeatMode(Player.REPEAT_MODE_ONE);

            playerView.getPlayer().seekTo(0, Math.min(startTime, playerView.getPlayer().getDuration()));
            playerView.getPlayer().setPlayWhenReady(true);
        }

        public void initializePlayer(String url, long startTime, ExoPlayer player) {
            /*player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mContext),
                    new DefaultTrackSelector(), new DefaultLoadControl());*/

            playerView.setPlayer(player);

            player.seekTo(0, Math.min(startTime, player.getDuration()));

            Uri uri = Uri.parse(url);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
            player.setRepeatMode(Player.REPEAT_MODE_ONE);

            //playerControl = new PlayerControl(player);
            hideSystemUi();
        }

        private MediaSource buildMediaSource(Uri uri) {
            return new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                    createMediaSource(uri);
        }

        @SuppressLint("InlinedApi")
        private void hideSystemUi() {
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        public void updateVisibility(boolean setVisible) {
            if (setVisible) {
                playerView.setVisibility(View.VISIBLE);
                thumbnail.setVisibility(View.GONE);
            } else {
                playerView.setVisibility(View.GONE);
                thumbnail.setVisibility(View.VISIBLE);
            }
        }
    }
}
