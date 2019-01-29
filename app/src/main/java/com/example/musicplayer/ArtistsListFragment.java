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
import android.widget.TextView;

import com.example.musicplayer.models.Album;
import com.example.musicplayer.models.Artist;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsListFragment extends Fragment {

    private MusicLab mMusicLab;

    private RecyclerView mRecyclerView;
    private ArtistAdapter mArtistAdapter;
    private List<Artist> mArtists;

    public static ArtistsListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ArtistsListFragment fragment = new ArtistsListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ArtistsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMusicLab = new MusicLab(getActivity());
        mArtists = mMusicLab.getArtists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artists_list, container, false);
        mRecyclerView = view.findViewById(R.id.artists_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mArtistAdapter == null){
            mArtistAdapter = new ArtistAdapter(mArtists);
            mRecyclerView.setAdapter(mArtistAdapter);
        }
        return view;
    }


    private class ArtistHolder extends RecyclerView.ViewHolder {

        private Artist mArtist;
        private TextView mArtistTitleTextView;

        public ArtistHolder(@NonNull View itemView) {
            super(itemView);

            mArtistTitleTextView = itemView.findViewById(R.id.music_artist_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AlbumTracksActivity.newIntent(getActivity(),mArtist.getArtist());
                    startActivity(intent);
                }
            });
        }

        public void bind(Artist artist){
            mArtist = artist;
            mArtistTitleTextView.setText(artist.getArtist());
        }
    }

    private class ArtistAdapter extends RecyclerView.Adapter<ArtistHolder> {

        private List<Artist> mArtists;

        public ArtistAdapter(List<Artist> artists) {
            mArtists = artists;
        }

        @NonNull
        @Override
        public ArtistHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_list_artist, viewGroup, false);
            ArtistHolder artistHolder = new ArtistHolder(view);
            return artistHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ArtistHolder artistHolder, int i) {
            Artist artist = mArtists.get(i);
            artistHolder.bind(artist);
        }

        @Override
        public int getItemCount() {
            return mArtists.size();
        }
    }

}
