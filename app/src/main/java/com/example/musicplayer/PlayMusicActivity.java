package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.musicplayer.models.Music;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayMusicActivity extends AppCompatActivity implements PlayMusicFragment.Callbacks{

    private static final String EXTRA_MUSIC_URI = "music_uri";
    private static final String EXTRA_ALBUM_ARTIST_NAME = "album_artist_name";
    private static final int NEXT_MUSIC = 0;
    private static final int PREVIOUS_MUSIC = 1;
    private static final int SHUFFLE_MUSIC = 2;

    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;

    private List<Music> mMusics;
    private Uri mCurrentMusicUri;

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

//        mViewPager = findViewById(R.id.play_music_view_pager);

        mCurrentMusicUri = getIntent().getParcelableExtra(EXTRA_MUSIC_URI);
        String name = getIntent().getStringExtra(EXTRA_ALBUM_ARTIST_NAME);

        mMusics = MusicLab.getInstance(this).getTracks(name);


//        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int i) {
//                Uri musicUri = mMusics.get(i).getUri();
//                return PlayMusicFragment.newInstance(musicUri);
//            }
//
//            @Override
//            public int getCount() {
//                return mMusics.size();
//            }
//
//        });
//
//
//        mViewPager.setCurrentItem(findCurrentItem(mCurrentMusicUri));


        mFragmentManager = getSupportFragmentManager();

        if (mFragmentManager.findFragmentById(R.id.play_music_view_pager) == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.play_music_view_pager,PlayMusicFragment.newInstance(mCurrentMusicUri))
                    .commit();
        }
    }

    @Override
    public void changeMusic(Uri musicUri, int action) {
        mCurrentMusicUri = musicUri;
        action(action);
    }

//    @Override
//    public boolean isCurrentItem(Uri musicUri) {
//        if (musicUri.equals(mCurrentMusicUri)){
//            return true;
//        }else {
//            return false;
//        }
//    }

    private void action(int action){
        if (action == NEXT_MUSIC){
            Iterator<Music> iterator = mMusics.iterator();
            while(!iterator.next().getUri().equals(mCurrentMusicUri)){
            }
            if (!iterator.hasNext()){
                iterator = mMusics.iterator();
            }
            Uri nextMusicUri = iterator.next().getUri();
//            mViewPager.setCurrentItem(findCurrentItem(nextMusicUri));

            mFragmentManager.beginTransaction()
                    .replace(R.id.play_music_view_pager,PlayMusicFragment.newInstance(nextMusicUri))
                    .commit();

        }else if (action == PREVIOUS_MUSIC){
            Iterator<Music> iterator = mMusics.iterator();
            while(!iterator.next().getUri().equals(mCurrentMusicUri)){
            }
            for (int i=0; i<mMusics.size()-2; i++){
                if (!iterator.hasNext()){
                    iterator = mMusics.iterator();
                }
                iterator.next();
            }
            if (!iterator.hasNext()){
                iterator = mMusics.iterator();
            }
            Uri nextMusicUri = iterator.next().getUri();
//            mViewPager.setCurrentItem(findCurrentItem(nextMusicUri));

            mFragmentManager.beginTransaction()
                    .replace(R.id.play_music_view_pager,PlayMusicFragment.newInstance(nextMusicUri))
                    .commit();

        }else if (action == SHUFFLE_MUSIC){
            Iterator<Music> iterator = mMusics.iterator();
            Random random = new Random();
            for (int i=0; i<random.nextInt(mMusics.size()); i++){
                if (!iterator.hasNext()){
                    iterator = mMusics.iterator();
                }
                iterator.next();
            }

            Uri nextMusicUri = iterator.next().getUri();
//            mViewPager.setCurrentItem(findCurrentItem(nextMusicUri));

            mFragmentManager.beginTransaction()
                    .replace(R.id.play_music_view_pager,PlayMusicFragment.newInstance(nextMusicUri))
                    .commit();
        }
    }

//    private int findCurrentItem(Uri uri){
//        for (int i = 0; i < mMusics.size(); i++) {
//            if (mMusics.get(i).getUri().equals(uri)) {
//                return i;
//            }
//        }
//
//        return 0;
//    }
}
