package com.example.musicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsListFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums_list, container, false);
    }

}
