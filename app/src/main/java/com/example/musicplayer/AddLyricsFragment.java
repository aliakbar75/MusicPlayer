package com.example.musicplayer;


import android.os.Bundle;
;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.musicplayer.models.Lyric;
import com.example.musicplayer.models.Music;
import com.example.musicplayer.models.MusicLab;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddLyricsFragment extends DialogFragment {

    private static final String ARG_MUSIC_ID = "music_id";
    private static final String ARG_CURRENT_POSITION = "current_position";
    private TextInputEditText mTextInputEditText;
    private TextInputEditText mDurationInputEditText;
    private MaterialButton mAddLyricMaterialButton;
    private MaterialButton mCancelMaterialButton;

    private Long mMusicId;
    private int mCurrentPosition;
//    private SeekBar mSeekBar;

    public static AddLyricsFragment newInstance(Long musicId,int currentPosition) {

        Bundle args = new Bundle();

        args.putLong(ARG_MUSIC_ID,musicId);
        args.putInt(ARG_CURRENT_POSITION,currentPosition);
        AddLyricsFragment fragment = new AddLyricsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public AddLyricsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, 550);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMusicId = getArguments().getLong(ARG_MUSIC_ID);
        mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_lyrics, container, false);

        mAddLyricMaterialButton = view.findViewById(R.id.add_lyrics);
        mCancelMaterialButton = view.findViewById(R.id.cancel);
        mTextInputEditText = view.findViewById(R.id.lyrics_text);
        mDurationInputEditText = view.findViewById(R.id.duration);
//        mSeekBar = view.findViewById(R.id.seek_bar);

        mAddLyricMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextInputEditText.getText();
                Lyric lyric = new Lyric();
                lyric.setMMusicId(mMusicId);
                lyric.setMLyric(mTextInputEditText.getText().toString());
                lyric.setMLyricTime(mCurrentPosition);
                if (mDurationInputEditText.getText().toString().length() == 0){
                    dismiss();
                    Toast.makeText(getActivity(), getString(R.string.not_added_lyric), Toast.LENGTH_SHORT).show();
                }else {
                    lyric.setMDuration(Integer.parseInt(mDurationInputEditText.getText().toString()));
                    if (MusicLab.getInstance(getActivity()).getLyric(mMusicId,lyric.getMLyricTime()) == null){
                        MusicLab.getInstance(getActivity()).addLyric(lyric);
                    }
                    dismiss();
                    Toast.makeText(getActivity(), getString(R.string.lyric_added), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCancelMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
