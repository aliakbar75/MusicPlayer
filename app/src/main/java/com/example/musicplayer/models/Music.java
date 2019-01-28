package com.example.musicplayer.models;

public class Music {
    private Long mId;
    private String mTitle;
    private String mAlbum;
    private String mArtist;

    public Music(Long id, String title, String album, String artist) {
        mId = id;
        mTitle = title;
        mAlbum = album;
        mArtist = artist;
    }

    public Long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }
}
