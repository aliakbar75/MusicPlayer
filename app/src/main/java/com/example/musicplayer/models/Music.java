package com.example.musicplayer.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

public class Music {
    private Long mId;
    private String mTitle;
    private String mAlbum;
    private String mArtist;
    private Uri mUri;

    public Music(Long id, String title, String album, String artist) {
        mId = id;
        mTitle = title;
        mAlbum = album;
        mArtist = artist;
        mUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

    }

    public Uri getUri() {
        return mUri;
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
