package com.example.musicplayer;


import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.models.Music;
import com.example.musicplayer.models.MusicLab;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    private static final String ARG_MUSIC_ID = "music_id";
    private static final String ARG_ALBUM_ARTIST_NAME = "album_artist_name";
    private static final String ARG_SEARCH_TYPE = "search_type";
    private static final String ARG_PAGE = "page";
    private static final String ARG_START = "start_page";

    private static final int PAGE_TRACKS = 0;
    private static final int PAGE_ALBUMS = 1;
    private static final int PAGE_ARTISTS = 2;
    private static final int PAGE_FAVORITES = 3;
    private static final int PAGE_SEARCH = 4;

    private ConstraintLayout mConstraintLayout;
    private ImageView mImageView;
    private ImageButton mNextImageButton;
    private ImageButton mPreviousImageButton;
    private ImageButton mPlayImageButton;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private Long mCurrentMusicId;

    private MediaPlayer mMediaPlayer;
    private Music mMusic;
    private List<Music> mMusics;
    private int mPage ;
    private Long mTrackId;
    private Uri mMusicUri;
    private int mSearchType;

    public static MusicFragment newInstance(Long musicId, String name, int page, int searchType, boolean start) {

        Bundle args = new Bundle();

        args.putLong(ARG_MUSIC_ID,musicId);
        args.putString(ARG_ALBUM_ARTIST_NAME,name);
        args.putInt(ARG_PAGE,page);
        args.putBoolean(ARG_START,start);
        args.putInt(ARG_SEARCH_TYPE,searchType);

        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentMusicId = getArguments().getLong(ARG_MUSIC_ID);
        mMusic = MusicLab.getInstance(getActivity()).getTrack(mCurrentMusicId);
        mPage = getArguments().getInt(ARG_PAGE);
        String name = getArguments().getString(ARG_ALBUM_ARTIST_NAME);
        mSearchType = getArguments().getInt(ARG_SEARCH_TYPE);


        switch (mPage){
            case PAGE_TRACKS:
                mMusics = MusicLab.getInstance(getActivity()).getTracks();
                break;
            case PAGE_ALBUMS:
                mMusics = MusicLab.getInstance(getActivity()).getTracksByAlbumArtistName(name,true);
                break;
            case PAGE_ARTISTS:
                mMusics = MusicLab.getInstance(getActivity()).getTracksByAlbumArtistName(name,false);
                break;
            case PAGE_FAVORITES:
                mMusics = MusicLab.getInstance(getActivity()).getFavoriteTracks();
                break;
            case PAGE_SEARCH:
                mMusics = MusicLab.getInstance(getActivity()).search(name,mSearchType);
                break;
            default:

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_music, container, false);

        final String name = getArguments().getString(ARG_ALBUM_ARTIST_NAME);
        boolean startPage = getArguments().getBoolean(ARG_START);

        mTitleTextView =view.findViewById(R.id.music_title);
        mArtistTextView =view.findViewById(R.id.music_artist);
        mNextImageButton =view.findViewById(R.id.next);
        mPreviousImageButton =view.findViewById(R.id.previous);
        mPlayImageButton =view.findViewById(R.id.play);
        mImageView = view.findViewById(R.id.music_image);
        mConstraintLayout = view.findViewById(R.id.current_music);

        startMusic(startPage);

        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Music> iterator = mMusics.iterator();
                while(!iterator.next().getMId().equals(mCurrentMusicId)){
                }
                if (!iterator.hasNext()){
                    iterator = mMusics.iterator();
                }
                mCurrentMusicId = iterator.next().getMId();

                startMusic(false);

            }
        });

        mPreviousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                startMusic(false);
            }
        });

        mPlayImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    mPlayImageButton.setImageResource(R.drawable.ic_play_music);
                }else{
                    mMediaPlayer.start();
                    mPlayImageButton.setImageResource(R.drawable.ic_pause_music);
                }
            }
        });


        mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMediaPlayer.pause();
                int currentPosition = mMediaPlayer.getCurrentPosition();
                switch (mPage){
                    case PAGE_TRACKS:
                        startActivity(null,PAGE_TRACKS,currentPosition);
                        break;
                    case PAGE_ALBUMS:
                        startActivity(name,PAGE_ALBUMS,currentPosition);
                        break;
                    case PAGE_ARTISTS:
                        startActivity(name,PAGE_ARTISTS,currentPosition);
                        break;
                    case PAGE_FAVORITES:
                        startActivity(null,PAGE_FAVORITES,currentPosition);
                        break;
                    case PAGE_SEARCH:
                        startActivity(name,PAGE_SEARCH,currentPosition);

                    default:

                }

            }
        });


        return view;
    }

    private void startMusic(boolean startPage) {

        mMusic = MusicLab.getInstance(getActivity()).getTrack(mCurrentMusicId);

        mTitleTextView.setText(mMusic.getMTitle());
        mArtistTextView.setText(mMusic.getMArtist());

        if (mMediaPlayer != null)
            mMediaPlayer.release();

        mTrackId = MusicLab.getInstance(getActivity()).getTrack(mCurrentMusicId).getMMusicId();
        mMusicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mTrackId);

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(getActivity(), mMusicUri);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!startPage){
            mMediaPlayer.start();
            mPlayImageButton.setImageResource(R.drawable.ic_pause_music);
        }

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        mediaMetadataRetriever.setDataSource(getActivity(),mMusicUri);
        rawArt = mediaMetadataRetriever.getEmbeddedPicture();
        if (rawArt != null){
            art = BitmapFactory.decodeByteArray(rawArt,0,rawArt.length,options);
        }

        mImageView.setImageBitmap(art);
    }

    private void startActivity(String albumArtistName, int page, int currentPosition){
        Intent intent = PlayMusicActivity.newIntent(getActivity(), mCurrentMusicId, albumArtistName, page, mSearchType,currentPosition);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }
}
