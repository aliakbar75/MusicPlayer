package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayMusicActivity extends AppCompatActivity {

    private static final String EXTRA_MUSIC_URI = "music_uri";
    private static final String EXTRA_ALBUM_ARTIST_NAME = "album_artist_name";

    public static Intent newIntent(Context context, Uri musicUri,String name){
        Intent intent = new Intent(context,PlayMusicActivity.class);
        intent.putExtra(EXTRA_MUSIC_URI,musicUri);
        intent.putExtra(EXTRA_ALBUM_ARTIST_NAME,name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Uri musicUri = getIntent().getParcelableExtra(EXTRA_MUSIC_URI);
        String name = getIntent().getStringExtra(EXTRA_ALBUM_ARTIST_NAME);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.play_music_fragment_container) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.play_music_fragment_container,PlayMusicFragment.newInstance(musicUri,name))
                    .commit();
        }
    }
}
