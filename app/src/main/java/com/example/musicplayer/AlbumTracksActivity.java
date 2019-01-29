package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AlbumTracksActivity extends AppCompatActivity {

    private static final String EXTRA_ALBUM_ARTIST_NAME = "album_artist_name";

    public static Intent newIntent(Context context, String name){
        Intent intent = new Intent(context,AlbumTracksActivity.class);
        intent.putExtra(EXTRA_ALBUM_ARTIST_NAME,name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_tracks);

        String name = getIntent().getStringExtra(EXTRA_ALBUM_ARTIST_NAME);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.tracks_fragment_container) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.tracks_fragment_container,TracksListFragment.newInstance(name))
                    .commit();
        }
    }
}
