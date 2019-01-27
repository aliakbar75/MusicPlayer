package com.example.musicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsListFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artists_list, container, false);
    }

}
