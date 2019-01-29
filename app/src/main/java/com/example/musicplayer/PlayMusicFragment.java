package com.example.musicplayer;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.musicplayer.models.Music;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {

    private static final String ARG_MUSIC_URI = "music_uri";
    private static final String ARG_ALBUM_ARTIST_NAME = "album_artist_name";

    private ImageButton mPlayButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private ImageButton mShuffleButton;
    private ImageButton mRepeatButton;
    private SeekBar mSeekBar;

    private Timer mTimer;
    private boolean mRepeatAll;

    private MediaPlayer mMediaPlayer;
    private MusicLab mMusicLab;
    private List<Music> mMusics;
    private String mAlbumArtistName;


    private Uri mMusicUri;

    public static PlayMusicFragment newInstance(Uri musicUri,String name) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_MUSIC_URI,musicUri);
        args.putString(ARG_ALBUM_ARTIST_NAME,name);
        PlayMusicFragment fragment = new PlayMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public PlayMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mMusicLab = MusicLab.getInstance(getActivity());
        mMusicUri = getArguments().getParcelable(ARG_MUSIC_URI);
        mAlbumArtistName = getArguments().getString(ARG_ALBUM_ARTIST_NAME);
        mMusics = mMusicLab.getTracks(mAlbumArtistName);
        mRepeatAll = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);

        mPlayButton = view.findViewById(R.id.play);
        mNextButton = view.findViewById(R.id.next);
        mPreviousButton = view.findViewById(R.id.previous);
        mShuffleButton = view.findViewById(R.id.shuffle);
        mRepeatButton = view.findViewById(R.id.repeat);
        mSeekBar = view.findViewById(R.id.seek_bar);


        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();

            try {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(getActivity(), mMusicUri);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaPlayer.start();
        }

//        final int[] currentPosition = new int[1];
//        currentPosition[0] = mMediaPlayer.getCurrentPosition();

        mSeekBar.setMax(mMediaPlayer.getDuration());

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mMediaPlayer != null){
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                }

            }
        },0,1000);

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

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mRepeatAll){
                    Iterator<Music> iterator = mMusics.iterator();
                    while(!iterator.next().getUri().equals(mMusicUri)){
                    }
                    if (!iterator.hasNext()){
                        iterator = mMusics.iterator();
                    }
                    Uri nextMusicUri = iterator.next().getUri();
                    Intent intent = PlayMusicActivity.newIntent(getActivity(),nextMusicUri, mAlbumArtistName);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    mMediaPlayer.start();
                }
            }
        });



        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Music> iterator = mMusics.iterator();
                while(!iterator.next().getUri().equals(mMusicUri)){
                }
                if (!iterator.hasNext()){
                    iterator = mMusics.iterator();
                }
                Uri nextMusicUri = iterator.next().getUri();
                Intent intent = PlayMusicActivity.newIntent(getActivity(),nextMusicUri, mAlbumArtistName);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Music> iterator = mMusics.iterator();
                while(!iterator.next().getUri().equals(mMusicUri)){
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
                Intent intent = PlayMusicActivity.newIntent(getActivity(),nextMusicUri, mAlbumArtistName);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Music> iterator = mMusics.iterator();
                Random random = new Random();
                for (int i=0; i<random.nextInt(mMusics.size()); i++){
                    if (!iterator.hasNext()){
                        iterator = mMusics.iterator();
                    }
                    iterator.next();
                }

                Uri nextMusicUri = iterator.next().getUri();
                Intent intent = PlayMusicActivity.newIntent(getActivity(),nextMusicUri, mAlbumArtistName);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRepeatAll){
                    mRepeatButton.setImageResource(R.drawable.ic_repeat_one);
                    mRepeatAll = false;
                }else {
                    mRepeatButton.setImageResource(R.drawable.ic_repeat_all);
                    mRepeatAll = true;
                }
            }
        });


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mMediaPlayer.release();
    }
}
