package com.example.musicplayer.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

public class Album {
    private Long mId;
    private String mAlbum;
    private String mArtist;
    private Uri mUri;

    public Album(Long id, String album, String artist) {
        mId = id;
        mAlbum = album;
        mArtist = artist;

        mUri = ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,id);
    }

    public Uri getUri() {
        return mUri;
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
