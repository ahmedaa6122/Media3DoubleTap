package com.ahmedaa612.media3doubletapexam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.net.Uri;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import com.ahmedaa612.media3doubletapexam.databinding.ActivityMainBinding;
import com.ahmedaa612.media3doubletap.dtpv.youtube.YouTubeOverlay;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private ExoPlayer exoplayer;

    private YouTubeOverlay youtubed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // set content view to binding's root
        setContentView(binding.getRoot());

        youtubed = findViewById(R.id.youtube_overlay1);

        exoplayer = new ExoPlayer.Builder(this).build();

        youtubed.player(exoplayer);
        
        binding.playerView.setPlayer(exoplayer);

        

        MediaItem mediaItem =
                MediaItem.fromUri(
                        Uri.parse(
                                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"));

        exoplayer.setMediaItem(mediaItem);

        exoplayer.prepare();

        exoplayer.setPlayWhenReady(true);

        youtubed.performListener(
                new YouTubeOverlay.PerformListener() {
                    @Override
                    public void onAnimationStart() {
                        youtubed.setAlpha(1.0f);
                        youtubed.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd() {
                        youtubed.animate()
                                .alpha(0.0f)
                                .setDuration(300)
                                .setListener(
                                        new AnimatorListenerAdapter() {

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                youtubed.setVisibility(View.GONE);
                                                youtubed.setAlpha(1.0f);
                                            }
                                        });
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}
