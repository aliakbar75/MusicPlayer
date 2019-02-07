package com.example.musicplayer;


import android.content.Intent;
import android.os.Bundle;
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

import com.example.musicplayer.models.Album;
import com.example.musicplayer.models.MusicLab;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsListFragment extends Fragment {

    private static final int PAGE_ALBUMS = 1;

    private MusicLab mMusicLab;

    private RecyclerView mRecyclerView;
    private AlbumAdapter mAlbumAdapter;
    private List<Album> mAlbums;

    public static AlbumsListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AlbumsListFragment fragment = new AlbumsListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public AlbumsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMusicLab = MusicLab.getInstance(getActivity());
        mAlbums = mMusicLab.getAlbums();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_albums_list, container, false);

        mRecyclerView = view.findViewById(R.id.albums_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mAlbumAdapter == null){
            mAlbumAdapter = new AlbumAdapter(mAlbums);
            mRecyclerView.setAdapter(mAlbumAdapter);
        }
        return view;
    }

    private class AlbumHolder extends RecyclerView.ViewHolder {

        private Album mAlbum;
        private TextView mMusicAlbumTextView;
        private TextView mMusicArtistTextView;
        private ImageView mAlbumImageView;

        public AlbumHolder(@NonNull View itemView) {
            super(itemView);

            mMusicAlbumTextView = itemView.findViewById(R.id.music_album_text_view);
            mMusicArtistTextView = itemView.findViewById(R.id.music_artist_text_view);

            mAlbumImageView = itemView.findViewById(R.id.music_cover_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AlbumTracksActivity.newIntent(getActivity(),mAlbum.getMAlbum(),PAGE_ALBUMS);
                    startActivity(intent);
                }
            });
        }

        public void bind(Album album){
            mAlbum = album;
            mMusicAlbumTextView.setText(album.getMAlbum());
            mMusicArtistTextView.setText(album.getMArtist());



//            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//            byte[] rawArt;
//            Bitmap art = null;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            mediaMetadataRetriever.setDataSource(getActivity(),mAlbum.getUri());
//            rawArt = mediaMetadataRetriever.getEmbeddedPicture();
//            if (rawArt != null){
//                art = BitmapFactory.decodeByteArray(rawArt,0,rawArt.length,options);
//            }
//
//            mAlbumImageView.setImageBitmap(art);
        }
    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

        private List<Album> mAlbums;

        public AlbumAdapter(List<Album> albums) {
            mAlbums = albums;
        }

        @NonNull
        @Override
        public AlbumHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_list_album, viewGroup, false);
            AlbumHolder albumHolder = new AlbumHolder(view);
            return albumHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumHolder albumHolder, int i) {
            Album album = mAlbums.get(i);
            albumHolder.bind(album);
        }

        @Override
        public int getItemCount() {
            return mAlbums.size();
        }
    }

}
