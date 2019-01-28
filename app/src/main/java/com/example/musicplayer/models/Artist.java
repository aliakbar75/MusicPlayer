package com.example.musicplayer.models;

public class Artist {

    private Long mId;
    private String mArtist;

    public Artist(Long id, String artist) {
        mId = id;
        mArtist = artist;
    }

    public Long getId() {
        return mId;
    }

    public String getArtist() {
        return mArtist;
    }
}
