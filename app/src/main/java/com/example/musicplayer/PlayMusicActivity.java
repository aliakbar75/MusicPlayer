package com.example.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.models.Music;
import com.example.musicplayer.models.MusicLab;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class PlayMusicActivity extends AppCompatActivity {

    private static final String EXTRA_MUSIC_ID = "music_id";
    private static final String EXTRA_ALBUM_ARTIST_NAME = "album_artist_name";
    private static final String EXTRA_SEARCH_TYPE = "search_type";
    private static final String EXTRA_PAGE = "page";
    private static final String EXTRA_CURRENT_POSITION = "current_position";
    private static final String ADD_LYRIC = "add_lyric";

    private static final int NEXT_MUSIC = 0;
    private static final int PREVIOUS_MUSIC = 1;
    private static final int SHUFFLE_MUSIC = 2;
    private static final int REPEAT_ALL = 0;
    private static final int REPEAT_ONE = 1;
    private static final int NO_REPEAT = 2;

    private static final int PAGE_TRACKS = 0;
    private static final int PAGE_ALBUMS = 1;
    private static final int PAGE_ARTISTS = 2;
    private static final int PAGE_FAVORITES = 3;
    private static final int PAGE_SEARCH = 4;

    private ImageButton mPlayButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private ImageButton mShuffleButton;
    private ImageButton mRepeatButton;
    private ImageButton mFavoriteButton;
    private ImageButton mPlayListButton;
    private ImageButton mLyricButton;

    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mMusicTimePastTextView;
    private TextView mMusicTotalTimeTextView;

    private ImageView mNoShuffle;
    private ImageView mNoRepeat;
    private SeekBar mSeekBar;

    private Handler mHandler;
    private int mRepeatAll;
    private boolean mShuffle;

    private MediaPlayer mMediaPlayer;
    private MusicLab mMusicLab;

    private ViewPager mViewPager;

    private List<Music> mMusics;
    private Long mCurrentMusicId;
    private Long mTrackId;
    private Uri mMusicUri;

    public static Intent newIntent(Context context, Long musicId, String name, int page, int searchType,int currentPosition){
        Intent intent = new Intent(context,PlayMusicActivity.class);
        intent.putExtra(EXTRA_MUSIC_ID,musicId);
        intent.putExtra(EXTRA_ALBUM_ARTIST_NAME,name);
        intent.putExtra(EXTRA_PAGE,page);
        intent.putExtra(EXTRA_SEARCH_TYPE,searchType);
        intent.putExtra(EXTRA_CURRENT_POSITION,currentPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        mViewPager = findViewById(R.id.play_music_view_pager);
        mPlayButton = findViewById(R.id.play);
        mNextButton = findViewById(R.id.next);
        mPreviousButton = findViewById(R.id.previous);
        mShuffleButton = findViewById(R.id.shuffle);
        mRepeatButton = findViewById(R.id.repeat);
        mFavoriteButton = findViewById(R.id.favorite);
        mPlayListButton = findViewById(R.id.play_list);
        mLyricButton = findViewById(R.id.lyrics);
        mSeekBar = findViewById(R.id.seek_bar);
        mTitleTextView = findViewById(R.id.music_title);
        mArtistTextView = findViewById(R.id.artist_name);
        mMusicTimePastTextView = findViewById(R.id.music_time_past);
        mMusicTotalTimeTextView = findViewById(R.id.music_total_time);
        mNoShuffle = findViewById(R.id.no_shuffle);
        mNoRepeat = findViewById(R.id.no_repeat);

        mMusicLab = MusicLab.getInstance(this);

        mRepeatAll = REPEAT_ALL;
        mShuffle = false;

        mCurrentMusicId = (Long) getIntent().getSerializableExtra(EXTRA_MUSIC_ID);

        int page = getIntent().getIntExtra(EXTRA_PAGE,0);
        String name = getIntent().getStringExtra(EXTRA_ALBUM_ARTIST_NAME);
        int searchType = getIntent().getIntExtra(EXTRA_SEARCH_TYPE,0);

        switch (page){
            case PAGE_TRACKS:
                mMusics = MusicLab.getInstance(this).getTracks();
                break;
            case PAGE_ALBUMS:
                mMusics = MusicLab.getInstance(this).getTracksByAlbumArtistName(name,true);
                break;
            case PAGE_ARTISTS:
                mMusics = MusicLab.getInstance(this).getTracksByAlbumArtistName(name,false);
                break;
            case PAGE_FAVORITES:
                mMusics = MusicLab.getInstance(this).getFavoriteTracks();
                break;
            case PAGE_SEARCH:
                mMusics = MusicLab.getInstance(this).search(name,searchType);
                break;
            default:

        }

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                Long musicId = mMusics.get(i).getMId();
                return PlayMusicFragment.newInstance(musicId);
            }

            @Override
            public int getCount() {
                return mMusics.size();
            }

        });

        mViewPager.setCurrentItem(findCurrentItem(mCurrentMusicId));

        mTitleTextView.setText(mMusicLab.getTrack(mCurrentMusicId).getMTitle());
        mArtistTextView.setText(mMusicLab.getTrack(mCurrentMusicId).getMArtist());

        if (mMusicLab.getTrack(mCurrentMusicId).getMIsFavorite()){
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_music);
        }else {
            mFavoriteButton.setImageResource(R.drawable.ic_no_favorite_music);
        }
        startMusic();

        String musicTotalTime = new SimpleDateFormat("mm:ss").format(mMediaPlayer.getDuration());
        mMusicTotalTimeTextView.setText(musicTotalTime.substring(1));



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                mCurrentMusicId = mMusics.get(i).getMId();

                mTitleTextView.setText(mMusicLab.getTrack(mCurrentMusicId).getMTitle());
                mArtistTextView.setText(mMusicLab.getTrack(mCurrentMusicId).getMArtist());

                if (mMusicLab.getTrack(mCurrentMusicId).getMIsFavorite()){
                    mFavoriteButton.setImageResource(R.drawable.ic_favorite_music);
                }else {
                    mFavoriteButton.setImageResource(R.drawable.ic_no_favorite_music);
                }

                mPlayButton.setImageResource(R.drawable.ic_pause_music);

                startMusic();

                String musicTotalTime = new SimpleDateFormat("mm:ss").format(mMediaPlayer.getDuration());
                mMusicTotalTimeTextView.setText(musicTotalTime.substring(1));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        handleButtonListeners();

    }

    private void startMusic() {

        if (mMediaPlayer != null)
            mMediaPlayer.release();

        mTrackId = mMusicLab.getTrack(mCurrentMusicId).getMMusicId();
        mMusicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mTrackId);

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(this, mMusicUri);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.start();

        handleSeekBar();

        mediaPlayerOnCompletionListener();
    }

    private void handleButtonListeners() {
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    mPlayButton.setImageResource(R.drawable.ic_play_music);
                }else{
                    mMediaPlayer.start();
                    mPlayButton.setImageResource(R.drawable.ic_pause_music);
                }
            }
        });


        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mShuffle){
                    changeMusic(SHUFFLE_MUSIC);
                }else {
                    changeMusic(NEXT_MUSIC);
                }
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeMusic(PREVIOUS_MUSIC);
            }
        });

        mShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mShuffle){
                    mNoShuffle.setVisibility(View.VISIBLE);
                    mShuffle = false;
                }else {
                    mNoShuffle.setVisibility(View.GONE);
                    mShuffle = true;
                }
            }
        });

        mRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRepeatAll==REPEAT_ALL){
                    mRepeatButton.setImageResource(R.drawable.ic_repeat_one);
                    mRepeatAll = REPEAT_ONE;
                }else if (mRepeatAll==REPEAT_ONE){
                    mNoRepeat.setVisibility(View.VISIBLE);
                    mRepeatAll = NO_REPEAT;
                }else if (mRepeatAll == NO_REPEAT){
                    mNoRepeat.setVisibility(View.GONE);
                    mRepeatButton.setImageResource(R.drawable.ic_repeat_all);
                    mRepeatAll = REPEAT_ALL;
                }
            }
        });

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {

            Music mMusic = mMusicLab.getTrack(mCurrentMusicId);
            @Override
            public void onClick(View v) {
                if(!mMusic.getMIsFavorite()){
                    mMusic.setMIsFavorite(true);
                    mMusicLab.updateMusic(mMusic);
                    mFavoriteButton.setImageResource(R.drawable.ic_favorite_music);
                }else {
                    mMusic.setMIsFavorite(false);
                    mMusicLab.updateMusic(mMusic);
                    mFavoriteButton.setImageResource(R.drawable.ic_no_favorite_music);
                }
            }
        });

        mPlayListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mLyricButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.pause();
                mPlayButton.setImageResource(R.drawable.ic_play_music);
                AddLyricsFragment addLyricsFragment = AddLyricsFragment.newInstance(mTrackId,mMediaPlayer.getCurrentPosition());
                addLyricsFragment.show(getSupportFragmentManager(),ADD_LYRIC);
            }
        });
    }

    private void mediaPlayerOnCompletionListener() {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mRepeatAll == REPEAT_ALL){
                    if (mShuffle){
                        changeMusic(SHUFFLE_MUSIC);
                    }else {
                        changeMusic(NEXT_MUSIC);
                    }
                }else if (mRepeatAll == REPEAT_ONE){
                    mMediaPlayer.start();
                }else {
                    mMediaPlayer.start();
                    mMediaPlayer.pause();
                    mPlayButton.setImageResource(R.drawable.ic_play_music);
                }
            }
        });
    }

    private void handleSeekBar() {
        mSeekBar.setMax(mMediaPlayer.getDuration());

        mHandler = new Handler();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(mMediaPlayer != null){
                        String musicPastTime = new SimpleDateFormat("mm:ss").format(mMediaPlayer.getCurrentPosition());
                        mMusicTimePastTextView.setText(musicPastTime.substring(1));
                        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                    mHandler.postDelayed(this, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

            }
        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mMediaPlayer != null){
                    mMediaPlayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private void changeMusic(int action){
        if (action == NEXT_MUSIC){

            Iterator<Music> iterator = mMusics.iterator();
            while(!iterator.next().getMId().equals(mCurrentMusicId)){
            }
            if (!iterator.hasNext()){
                iterator = mMusics.iterator();
            }
            mCurrentMusicId = iterator.next().getMId();
            mViewPager.setCurrentItem(findCurrentItem(mCurrentMusicId));

        }else if (action == PREVIOUS_MUSIC){
            Iterator<Music> iterator = mMusics.iterator();
            while(!iterator.next().getMId().equals(mCurrentMusicId)){
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

            mCurrentMusicId = iterator.next().getMId();
            mViewPager.setCurrentItem(findCurrentItem(mCurrentMusicId));

        }else if (action == SHUFFLE_MUSIC){
            Iterator<Music> iterator = mMusics.iterator();
            Random random = new Random();
            for (int i=0; i<random.nextInt(mMusics.size()); i++){
                if (!iterator.hasNext()){
                    iterator = mMusics.iterator();
                }
                iterator.next();
            }

            mCurrentMusicId = iterator.next().getMId();
            mViewPager.setCurrentItem(findCurrentItem(mCurrentMusicId));
        }
    }

    private int findCurrentItem(Long id){
        for (int i = 0; i < mMusics.size(); i++) {
            if (mMusics.get(i).getMId().equals(id)) {
                return i;
            }
        }

        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }
}
