package com.bowoon.android.android_videoview.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.logcat.log.ALog;
import com.bowoon.android.android_videoview.R;
import com.bowoon.android.android_videoview.vo.Item;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayerActivity extends Activity {
    private Item item;
    private Button serviceStartBtn, dialogBtn;
    private TextView videoTitle;
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    private MediaSource mediaSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exoplayer_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("videoContent");

        initView();
        setVideo(Uri.parse(item.getPath()));
    }

    private void initView() {
        serviceStartBtn = (Button) findViewById(R.id.video_service);
        dialogBtn = (Button) findViewById(R.id.make_gif);
        videoTitle = (TextView) findViewById(R.id.play_video_title);
        videoTitle.setText(item.getTitle());
        playerView = (PlayerView) findViewById(R.id.main_player_view);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, getDefaultTrackSelector());
        playerView.setPlayer(exoPlayer);

        serviceStartBtn.setOnClickListener(listener);
        dialogBtn.setOnClickListener(listener);
    }

    public void setVideo(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "yourApplicationName"), new DefaultBandwidthMeter());

        mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    public TrackSelector getDefaultTrackSelector() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        return trackSelector;
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_player_view:
                    videoTitle.setVisibility(View.VISIBLE);
                    serviceStartBtn.setVisibility(View.VISIBLE);
                    dialogBtn.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            videoTitle.setVisibility(View.GONE);
                            serviceStartBtn.setVisibility(View.GONE);
                            dialogBtn.setVisibility(View.GONE);
                        }
                    }, 3000);
                    break;
                case R.id.video_service:
                    ALog.i("startService");
                    Intent serviceIntent = new Intent(getApplicationContext(), VideoService.class);
                    serviceIntent.putExtra("video", item);
                    serviceIntent.putExtra("currentTime", exoPlayer.getCurrentPosition());
                    ALog.i(exoPlayer.getCurrentPosition());
                    startService(serviceIntent);
                    finish();
                    break;
                case R.id.make_gif:
                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View v = inflater.inflate(R.layout.dialog_layout, null);
                    builder.setView(v);
                    Button make = (Button) v.findViewById(R.id.gif_extractor);
                    final EditText start = (EditText) v.findViewById(R.id.gif_start);
                    final EditText end = (EditText) v.findViewById(R.id.gif_end);
                    final AlertDialog dialog = builder.create();
                    make.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            startTime = start.getText().toString();
//                            endTime = end.getText().toString();
//
//                            GIFExtractor extractor = new GIFExtractor(exoPlayer.getDuration());
//
//                            Intent intent = new Intent(VideoPlayerActivity.this, GIFView.class);
//                            intent.putExtra("gifImage", extractor.makeGIF(item.getPath(), Long.valueOf(startTime), Long.valueOf(endTime)));
//                            startActivity(intent);
//
//                            mediaPlayer.start();
//                            seekBarFlag = true;
//                            new ProgressSeekBar().start();
                            dialog.dismiss();
                        }
                    });

                    ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

                    dialog.show();
                    break;
                default:
                    break;
            }
        }
    };


    private void releaseMediaPlayer() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ALog.i("onPause");
    }

    @Override
    protected void onResume() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
        ALog.i("onResume");
    }

    @Override
    protected void onStop() {
        releaseMediaPlayer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        releaseMediaPlayer();
        super.onDestroy();
    }
}
