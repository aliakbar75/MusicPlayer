package com.example.musicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesListFragment extends Fragment {

    public static FavoritesListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FavoritesListFragment fragment = new FavoritesListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public FavoritesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_list, container, false);
    }

}
