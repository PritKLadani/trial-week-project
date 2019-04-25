package com.felipecsl.asymmetricgridview.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.felipecsl.asymmetricgridview.AGVRecyclerViewAdapter;
import com.felipecsl.asymmetricgridview.AsymmetricItem;
import com.felipecsl.asymmetricgridview.app.model.DemoItem;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;

class RecyclerViewAdapter extends AGVRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private final List<DemoItem> items;
    private final int TYPE_VIDEO = 1;
    private final int TYPE_IMAGE = 0;

    RecyclerViewAdapter(List<DemoItem> items, Context context) {
        this.items = items;
        mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public class MyViewHolderVideo extends RecyclerView.ViewHolder {

        PlayerView playerView;
        SimpleExoPlayer player;

        public MyViewHolderVideo(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.video_view_holder_exoplayer);

//            playerView.setControllerAutoShow(false);
            playerView.setUseController(false);

            playerView.getVideoSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoViewActivity.class);
                    mContext.startActivity(intent);
                }
            });

            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

            initializePlayer();
        }

        private void initializePlayer() {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mContext),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            playerView.setPlayer(player);

            player.setPlayWhenReady(false);
            player.seekTo(0, 0);

            Uri uri = Uri.parse("https://sample-videos.com/video123/mp4/240/big_buck_bunny_240p_30mb.mp4");
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);

            player.setVolume(0f);
        }

        private MediaSource buildMediaSource(Uri uri) {
            return new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                    createMediaSource(uri);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclerViewActivity", "onCreateViewHolder viewType = " + viewType);
        if (viewType == TYPE_IMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            return new MyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new MyViewHolderVideo(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        Log.d("RecyclerViewActivity", "onBindView position=" + position + " viewType = " + viewType + " isInstanceof(video) " + (holder instanceof MyViewHolderVideo));

        if (holder instanceof MyViewHolder) {
            DemoItem item = items.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            Glide.with(mContext)
                    .load(item.getUrl())
                    .override(150, 150)
                    .centerCrop()
                    .into(myViewHolder.imageView);
        } else if (holder instanceof MyViewHolderVideo) {
            DemoItem item = items.get(position);
            MyViewHolderVideo myViewHolderVideo = (MyViewHolderVideo) holder;
            myViewHolderVideo.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoViewActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AsymmetricItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getColumnSpan() == 2) {
            return TYPE_VIDEO;
        }
        return TYPE_IMAGE;
    }
}