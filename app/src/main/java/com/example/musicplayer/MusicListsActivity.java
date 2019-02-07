package com.example.musicplayer;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.musicplayer.models.MusicLab;

public class MusicListsActivity extends AppCompatActivity implements TracksListFragment.Callbacks{

    private static final int FAVORITES = 0;
    private static final int TRACKS = 1;
    private static final int ALBUMS = 2;
    private static final int ARTISTS = 3;
    private static final String DIALOG_SEARCH = "dialog_search";

    private static final int PAGE_TRACKS = 0;

    private TabLayout mTabLayout;
    private ViewPager mMusicViewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_music_lists, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SearchDialogFragment searchDialogFragment = SearchDialogFragment.newInstance();
        searchDialogFragment.show(getSupportFragmentManager(),DIALOG_SEARCH);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lists);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Long firstMusicId = MusicLab.getInstance(this).getTracks().get(0).getMId();

        if (fragmentManager.findFragmentById(R.id.music_container) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.music_container,MusicFragment.newInstance(firstMusicId,null,PAGE_TRACKS,0))
                    .commit();
        }

        mTabLayout = findViewById(R.id.tab_layout);
        mMusicViewPager = findViewById(R.id.music_view_pager);

        mTabLayout.setupWithViewPager(mMusicViewPager);

        mMusicViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {

                if(i==FAVORITES)
                    return FavoritesListFragment.newInstance();
                if(i==TRACKS)
                    return TracksListFragment.newInstance(null,PAGE_TRACKS);
                if(i==ALBUMS)
                    return AlbumsListFragment.newInstance();
                if(i==ARTISTS)
                    return ArtistsListFragment.newInstance();

                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                if (position==FAVORITES)
                    return getString(R.string.favorites_list_title);
                if (position==TRACKS)
                    return getString(R.string.tracks_list_title);
                if (position==ALBUMS)
                    return getString(R.string.albums_list_title);
                if (position==ARTISTS)
                    return getString(R.string.artists_list_title);

                return super.getPageTitle(position);
            }
        });

        mMusicViewPager.setCurrentItem(TRACKS);

    }


    @Override
    public void changeMusic(Long musicId, String name, int page) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.music_container,MusicFragment.newInstance(musicId,name,page,0))
                .commit();
    }
}
