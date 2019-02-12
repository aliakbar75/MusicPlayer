package com.example.musicplayer;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.models.Music;
import com.example.musicplayer.models.MusicLab;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TracksListFragment extends Fragment {

    private static final String ARG_ALBUM_ARTIST_NAME = "album_artist_name";
    private static final String ARG_PAGE = "page";
//    private static final String ARG_IS_ALBUM_ARTIST_LIST = "is_album_artist_list";
//    private static final String ARG_IS_ALBUM = "is_album";

    private static final int PAGE_TRACKS = 0;
    private static final int PAGE_ALBUMS = 1;
    private static final int PAGE_ARTISTS = 2;


    private MusicLab mMusicLab;

    private RecyclerView mRecyclerView;
    private MusicAdapter mMusicAdapter;
    private List<Music> mMusics;
    private String mAlbumArtistName;
    private int mPage;
//    private boolean mIsAlbumArtistList;
//    private boolean mIsAlbum;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void changeMusic(Long musicId, String name, int page);
    }


    public static TracksListFragment newInstance(String name,int page) {
        
        Bundle args = new Bundle();
        args.putString(ARG_ALBUM_ARTIST_NAME,name);
        args.putInt(ARG_PAGE,page);
//        args.putBoolean(ARG_IS_ALBUM_ARTIST_LIST,isAlbumArtistList);
//        args.putBoolean(ARG_IS_ALBUM,isAlbum);
        TracksListFragment fragment = new TracksListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public TracksListFragment() {
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

        mAlbumArtistName = getArguments().getString(ARG_ALBUM_ARTIST_NAME);
        mPage = getArguments().getInt(ARG_PAGE);
//        mIsAlbumArtistList = getArguments().getBoolean(ARG_IS_ALBUM_ARTIST_LIST);
//        mIsAlbum = getArguments().getBoolean(ARG_IS_ALBUM);
        mMusicLab = MusicLab.getInstance(getActivity());

        if (mPage == PAGE_TRACKS){
            mMusics = mMusicLab.getTracks();
        }else if (mPage == PAGE_ALBUMS){
            mMusics = mMusicLab.getTracksByAlbumArtistName(mAlbumArtistName,true);
        }else {
            mMusics = mMusicLab.getTracksByAlbumArtistName(mAlbumArtistName,false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracks_list, container, false);
        mRecyclerView = view.findViewById(R.id.tracks_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mMusicAdapter == null){
            mMusicAdapter = new MusicAdapter(mMusics);
            mRecyclerView.setAdapter(mMusicAdapter);
        }
//        else {
//            mMusicAdapter.setTasks(mTasks);
//            mMusicAdapter.notifyDataSetChanged();
//        }
        return view;
    }

    private class MusicHolder extends RecyclerView.ViewHolder {

        private Music mMusic;
        private TextView mMusicTitleTextView;
        private TextView mMusicAlbumTextView;
        private TextView mMusicArtistTextView;
        private ImageView mMusicImageView;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);

            mMusicTitleTextView = itemView.findViewById(R.id.music_title_text_view);
            mMusicAlbumTextView = itemView.findViewById(R.id.music_album_text_view);
            mMusicArtistTextView = itemView.findViewById(R.id.music_artist_text_view);
            mMusicImageView = itemView.findViewById(R.id.music_cover_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.changeMusic(mMusic.getMId(),mAlbumArtistName,mPage);
//                    Intent intent = PlayMusicActivity.newIntent(getActivity(),
//                            mMusic.getMId(),
//                            mAlbumArtistName,
//                            mPage,
//                            0);
//                    startActivity(intent);
                }
            });
        }

        public void bind(Music music){
            mMusic = music;
            mMusicTitleTextView.setText(music.getMTitle());
            mMusicAlbumTextView.setText(music.getMAlbum());
            mMusicArtistTextView.setText(music.getMArtist());

            Uri musicUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, music.getMMusicId());

            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            byte[] rawArt;
            Bitmap art = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            mediaMetadataRetriever.setDataSource(getActivity(),musicUri);
            rawArt = mediaMetadataRetriever.getEmbeddedPicture();
            if (rawArt != null){
                art = BitmapFactory.decodeByteArray(rawArt,0,rawArt.length,options);
            }

            mMusicImageView.setImageBitmap(art);
        }
    }

    private class MusicAdapter extends RecyclerView.Adapter<MusicHolder> {

        private List<Music> mMusics;

        public MusicAdapter(List<Music> musics) {
            mMusics = musics;
        }

        @NonNull
        @Override
        public MusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_list_track, viewGroup, false);
            MusicHolder musicHolder = new MusicHolder(view);
            return musicHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MusicHolder musicHolder, int i) {
            Music music = mMusics.get(i);
            musicHolder.bind(music);
        }

        @Override
        public int getItemCount() {
            return mMusics.size();
        }
    }

}
