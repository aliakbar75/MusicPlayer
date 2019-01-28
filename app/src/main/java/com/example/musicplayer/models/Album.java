package com.example.musicplayer.models;

public class Album {
    private Long mId;
    private String mAlbum;
    private String mArtist;

    public Album(Long id, String album, String artist) {
        mId = id;
        mAlbum = album;
        mArtist = artist;
    }

    public Long getId() {
        return mId;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }
}
