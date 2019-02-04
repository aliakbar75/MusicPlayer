package com.example.musicplayer;


import android.content.ContentUris;
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
    private static final String ARG_IS_ALBUM_ARTIST_LIST = "is_album_artist_list";
    private static final String ARG_IS_ALBUM = "is_album";
    private MusicLab mMusicLab;

    private RecyclerView mRecyclerView;
    private MusicAdapter mMusicAdapter;
    private List<Music> mMusics;
    private String mAlbumArtistName;
    private boolean mIsAlbumArtistList;
    private boolean mIsAlbum;


    public static TracksListFragment newInstance(String name,boolean isAlbumArtistList,boolean isAlbum) {
        
        Bundle args = new Bundle();
        args.putString(ARG_ALBUM_ARTIST_NAME,name);
        args.putBoolean(ARG_IS_ALBUM_ARTIST_LIST,isAlbumArtistList);
        args.putBoolean(ARG_IS_ALBUM,isAlbum);
        TracksListFragment fragment = new TracksListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public TracksListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAlbumArtistName = getArguments().getString(ARG_ALBUM_ARTIST_NAME);
        mIsAlbumArtistList = getArguments().getBoolean(ARG_IS_ALBUM_ARTIST_LIST);
        mIsAlbum = getArguments().getBoolean(ARG_IS_ALBUM);
        mMusicLab = MusicLab.getInstance(getActivity());
        if (!mIsAlbumArtistList){
            mMusics = mMusicLab.getTracks();
        }else {
            mMusics = mMusicLab.getTracksByAlbumArtistName(mAlbumArtistName,mIsAlbum);
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
        private ImageView mMusicImageView;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);

            mMusicTitleTextView = itemView.findViewById(R.id.music_title_text_view);
            mMusicImageView = itemView.findViewById(R.id.music_cover_image);





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PlayMusicActivity.newIntent(getActivity(),mMusic.getMId(), mAlbumArtistName,mIsAlbumArtistList,mIsAlbum);
                    startActivity(intent);
                }
            });
        }

        public void bind(Music music){
            mMusic = music;
            mMusicTitleTextView.setText(music.getMTitle());

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
