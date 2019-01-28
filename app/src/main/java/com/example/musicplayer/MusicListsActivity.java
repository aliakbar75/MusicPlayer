package com.example.musicplayer;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MusicListsActivity extends AppCompatActivity {

    private static final int TRACKS = 0;
    private static final int ALBUMS = 1;
    private static final int ARTISTS = 2;

    private TabLayout mTabLayout;
    private ViewPager mMusicViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lists);
        mTabLayout = findViewById(R.id.tab_layout);
        mMusicViewPager = findViewById(R.id.music_view_pager);

        mTabLayout.setupWithViewPager(mMusicViewPager);

        mMusicViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {

                if(i==TRACKS)
                    return TracksListFragment.newInstance();
                if(i==ALBUMS)
                    return AlbumsListFragment.newInstance();
                if(i==ARTISTS)
                    return ArtistsListFragment.newInstance();

                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                if(position==TRACKS)
                    return getString(R.string.tracks_list_title);
                if (position==ALBUMS)
                    return getString(R.string.albums_list_title);
                if (position==ARTISTS)
                    return getString(R.string.artists_list_title);

                return super.getPageTitle(position);
            }
        });
    }
}
