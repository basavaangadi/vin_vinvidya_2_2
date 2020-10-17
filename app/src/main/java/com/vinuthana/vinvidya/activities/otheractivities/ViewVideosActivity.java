package com.vinuthana.vinvidya.activities.otheractivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vinuthana.vinvidya.R;

public class ViewVideosActivity extends AppCompatActivity {
String strVideo_link="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_videos);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        strVideo_link = bundle.getString("video_link");

       /* YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "eWEF1Zrmdow";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });*/
    }
}
