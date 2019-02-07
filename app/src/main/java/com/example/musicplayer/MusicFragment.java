package com.example.musicplayer;


import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    private static final String ARG_MUSIC_ID = "music_id";
    private static final String ARG_ALBUM_ARTIST_NAME = "album_artist_name";
    private static final String ARG_IS_ALBUM_ARTIST_LIST = "is_album_artist_list";
    private static final String ARG_IS_ALBUM = "is_album";
    private static final String ARG_IS_FAVORITE = "is_favorite";
    private static final String ARG_IS_SEARCH = "is_search";
    private static final String ARG_SEARCH_TYPE = "search_type";
    private static final String ARG_PAGE = "page";

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

    private Music mMusic;

    public static MusicFragment newInstance(Long musicId,
                                            String name,
                                            int page,
                                            int searchType) {

        Bundle args = new Bundle();

        args.putLong(ARG_MUSIC_ID,musicId);
        args.putString(ARG_ALBUM_ARTIST_NAME,name);
        args.putInt(ARG_PAGE,page);
//        args.putBoolean(ARG_IS_ALBUM_ARTIST_LIST,isAlbumArtistList);
//        args.putBoolean(ARG_IS_ALBUM,isAlbum);
//        args.putBoolean(ARG_IS_FAVORITE,isFavorite);
//        args.putBoolean(ARG_IS_SEARCH,isSearch);
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

        mCurrentMusicId = (Long) getArguments().getLong(ARG_MUSIC_ID);
        mMusic = MusicLab.getInstance(getActivity()).getTrack(mCurrentMusicId);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_music, container, false);

        Long musicId = getArguments().getLong(ARG_MUSIC_ID);
        final String name = getArguments().getString(ARG_ALBUM_ARTIST_NAME);
        final int page = getArguments().getInt(ARG_PAGE);
//        final boolean isAlbumArtistList = getArguments().getBoolean(ARG_IS_ALBUM_ARTIST_LIST,false);
//        final boolean isAlbum = getArguments().getBoolean(ARG_IS_ALBUM,false);
//        final boolean isFavorite = getArguments().getBoolean(ARG_IS_FAVORITE,false);
//        final boolean isSearch = getArguments().getBoolean(ARG_IS_SEARCH,false);
        final int searchType = getArguments().getInt(ARG_SEARCH_TYPE,0);

        mTitleTextView =view.findViewById(R.id.music_title);
        mArtistTextView =view.findViewById(R.id.music_artist);
        mNextImageButton =view.findViewById(R.id.next);
        mPreviousImageButton =view.findViewById(R.id.previous);
        mPlayImageButton =view.findViewById(R.id.play);
        mImageView = view.findViewById(R.id.music_image);
        mConstraintLayout = view.findViewById(R.id.current_music);

        Long trackId = MusicLab.getInstance(getActivity()).getTrack(musicId).getMMusicId();

        Uri musicUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, trackId);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        mediaMetadataRetriever.setDataSource(getActivity(),musicUri);
        rawArt = mediaMetadataRetriever.getEmbeddedPicture();
        if (rawArt != null){
            art = BitmapFactory.decodeByteArray(rawArt,0,rawArt.length,options);
        }

        mImageView.setImageBitmap(art);


        mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (page){
                    case PAGE_TRACKS:
                        startActivity(null,PAGE_TRACKS);
                        break;
                    case PAGE_ALBUMS:
                        startActivity(name,PAGE_ALBUMS);
                        break;
                    case PAGE_ARTISTS:
                        startActivity(name,PAGE_ARTISTS);
                        break;
                    case PAGE_FAVORITES:
                        startActivity(null,PAGE_FAVORITES);
                        break;
                    default:

                }

            }
        });


        return view;
    }

    private void startActivity(String albumArtistName,
                                int page){
        Intent intent = PlayMusicActivity.newIntent(getActivity(), mMusic.getMId(), albumArtistName, page, 0);
        startActivity(intent);
    }

}
