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
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.musicplayer.models.Music;
import com.example.musicplayer.models.MusicLab;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDialogFragment extends DialogFragment {

    private static final int PAGE_SEARCH = 4;

    private TextInputEditText mTextInputEditText;
    private RadioGroup mRadioGroup;
    private RadioButton mTracksSearchRadioButton;
    private RadioButton mAlbumsSearchRadioButton;
    private RadioButton mArtistsSearchRadioButton;
    private ImageButton mSearchImageButton;
    private RecyclerView mRecyclerView;

    private MusicAdapter mMusicAdapter;
    private List<Music> mMusics;
    private int mSearchType;
    private final String[] mSearchText = new String[1];




    public static SearchDialogFragment newInstance() {

        Bundle args = new Bundle();

        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public SearchDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_dialog, container, false);
        mTextInputEditText = view.findViewById(R.id.search_text);
        mTracksSearchRadioButton = view.findViewById(R.id.search_tracks);
        mAlbumsSearchRadioButton = view.findViewById(R.id.search_albums);
        mArtistsSearchRadioButton = view.findViewById(R.id.search_artists);
        mSearchImageButton = view.findViewById(R.id.search_button);
        mRecyclerView = view.findViewById(R.id.search_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMusics = MusicLab.getInstance(getActivity()).search(mSearchText[0], mSearchType);

        mTracksSearchRadioButton.setChecked(true);

        mSearchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText[0] = mTextInputEditText.getText().toString();

                if (mTracksSearchRadioButton.isChecked()){
                    mSearchType = 0;
                }else if (mAlbumsSearchRadioButton.isChecked()){
                    mSearchType = 1;
                }else if (mArtistsSearchRadioButton.isChecked()){
                    mSearchType = 2;
                }
                mMusics = MusicLab.getInstance(getActivity()).search(mSearchText[0], mSearchType);

                if (mMusicAdapter == null){
                    mMusicAdapter = new MusicAdapter(mMusics);
                    mRecyclerView.setAdapter(mMusicAdapter);
                }else {
                    mMusicAdapter.setMusics(mMusics);
                    mMusicAdapter.notifyDataSetChanged();
                }

            }
        });

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
                    Intent intent = PlayMusicActivity.newIntent(getActivity(),
                            mMusic.getMId(),
                            mSearchText[0],
                            PAGE_SEARCH,
                            mSearchType);
                    startActivity(intent);
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

        public void setMusics(List<Music> musics) {
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
