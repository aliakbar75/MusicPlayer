package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayMusicActivity extends AppCompatActivity {

    private static final String EXTRA_MUSIC_URI = "music_uri";

    public static Intent newIntent(Context context, Uri musicUri){
        Intent intent = new Intent(context,PlayMusicActivity.class);
        intent.putExtra(EXTRA_MUSIC_URI,musicUri);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Uri musicUri = getIntent().getParcelableExtra(EXTRA_MUSIC_URI);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.play_music_fragment_container) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.play_music_fragment_container,PlayMusicFragment.newInstance(musicUri))
                    .commit();
        }
    }
}
