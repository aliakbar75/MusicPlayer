package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AlbumTracksActivity extends AppCompatActivity implements TracksListFragment.Callbacks{

    private static final String EXTRA_ALBUM_ARTIST_NAME = "album_artist_name";
    private static final String EXTRA_IS_ALBUM = "is_album";
    private static final String EXTRA_PAGE = "page";

    public static Intent newIntent(Context context, String name,int page){
        Intent intent = new Intent(context,AlbumTracksActivity.class);
        intent.putExtra(EXTRA_ALBUM_ARTIST_NAME,name);
        intent.putExtra(EXTRA_PAGE,page);
//        intent.putExtra(EXTRA_IS_ALBUM,isAlbum);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_tracks);

        String name = getIntent().getStringExtra(EXTRA_ALBUM_ARTIST_NAME);
        int page = getIntent().getIntExtra(EXTRA_PAGE,0);
        boolean isAlbum = getIntent().getBooleanExtra(EXTRA_IS_ALBUM,false);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.tracks_fragment_container) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.tracks_fragment_container,TracksListFragment.newInstance(name,page))
                    .commit();
        }
    }

    @Override
    public void changeMusic(Long musicId, String name, int page) {

    }
}
