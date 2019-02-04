package com.example.musicplayer;


import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.models.MusicLab;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {

    private static final String ARG_MUSIC_ID = "music_id";
//    private static final String ARG_ALBUM_ARTIST_NAME = "album_artist_name";
    private static final int NEXT_MUSIC = 0;
    private static final int PREVIOUS_MUSIC = 1;
    private static final int SHUFFLE_MUSIC = 2;

    private ImageButton mPlayButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private ImageButton mShuffleButton;
    private ImageButton mRepeatButton;
    private ImageButton mFavoriteButton;
    private ImageButton mPlayListButton;

    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mMusicTimePastTextView;
    private TextView mMusicTotalTimeTextView;

    private ImageView mMusicImageView;
    private SeekBar mSeekBar;

    private Timer mTimer;
    private Handler mHandler;
    private boolean mRepeatAll;

    private MediaPlayer mMediaPlayer;
    private MusicLab mMusicLab;
//    private List<Music> mMusics;
//    private String mAlbumArtistName;

    private Long mMusicId;
    private Uri mMusicUri;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void changeMusic(Long musicId,int action);
//        boolean isCurrentItem(Uri musicUri);
    }

    public static PlayMusicFragment newInstance(Long musicId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_MUSIC_ID,musicId);
//        args.putString(ARG_ALBUM_ARTIST_NAME,name);
        PlayMusicFragment fragment = new PlayMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public PlayMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            throw new RuntimeException("Activity not impl callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mMusicLab = MusicLab.getInstance(getActivity());
        mMusicId = (Long) getArguments().getSerializable(ARG_MUSIC_ID);
//        mAlbumArtistName = getArguments().getString(ARG_ALBUM_ARTIST_NAME);
//        mMusics = mMusicLab.getTracks(mAlbumArtistName);
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
        mFavoriteButton = view.findViewById(R.id.favorite);
        mPlayListButton = view.findViewById(R.id.play_list);
        mSeekBar = view.findViewById(R.id.seek_bar);
        mTitleTextView = view.findViewById(R.id.music_title);
        mArtistTextView = view.findViewById(R.id.artist_name);
        mMusicTimePastTextView = view.findViewById(R.id.music_time_past);
        mMusicTotalTimeTextView = view.findViewById(R.id.music_total_time);
        mMusicImageView = view.findViewById(R.id.music_image);

        Long musicId = mMusicLab.getTrack(mMusicId).getMMusicId();
        mMusicUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicId);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        mediaMetadataRetriever.setDataSource(getActivity(),mMusicUri);
        rawArt = mediaMetadataRetriever.getEmbeddedPicture();
        if (rawArt != null){
            art = BitmapFactory.decodeByteArray(rawArt,0,rawArt.length,options);
        }

        mMusicImageView.setImageBitmap(art);

        String title = mMusicLab.getTrack(mMusicId).getMTitle();
        String artistName = mMusicLab.getTrack(mMusicId).getMArtist();
        mTitleTextView.setText(title);
        mArtistTextView.setText(artistName);

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

//            if (mCallbacks.isCurrentItem(mMusicUri)){
//
//            }
        }

        String musicTotalTime = new SimpleDateFormat("mm:ss").format(mMediaPlayer.getDuration());
        mMusicTotalTimeTextView.setText(musicTotalTime.substring(1));

        handleSeekBar();

        handleButtonListeners();


        return view;
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

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mRepeatAll){
                    mCallbacks.changeMusic(mMusicId,NEXT_MUSIC);

                }else {
                    mMediaPlayer.start();
                }
            }
        });


        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.changeMusic(mMusicId,NEXT_MUSIC);
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallbacks.changeMusic(mMusicId,PREVIOUS_MUSIC);
            }
        });

        mShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallbacks.changeMusic(mMusicId,SHUFFLE_MUSIC);
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

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPlayListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void handleSeekBar() {
        mSeekBar.setMax(mMediaPlayer.getDuration());

        mHandler = new Handler();

        getActivity().runOnUiThread(new Runnable() {
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

//        mTimer = new Timer();
//        mTimer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (mMediaPlayer != null){
//                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
//                }
//
//            }
//        },0,1000);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mTimer.cancel();
        mMediaPlayer.release();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mTimer.cancel();
    }
}
