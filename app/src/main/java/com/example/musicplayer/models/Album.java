package com.example.musicplayer.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Album {
    @Id(autoincrement = true)
    private Long mId;
    private Long mAlbumId;
    private String mAlbum;
    private String mArtist;
//    private Uri mUri;

    @Generated(hash = 1665280022)
    public Album(Long mId, Long mAlbumId, String mAlbum, String mArtist) {
        this.mId = mId;
        this.mAlbumId = mAlbumId;
        this.mAlbum = mAlbum;
        this.mArtist = mArtist;
    }
    @Generated(hash = 1609191978)
    public Album() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public String getMAlbum() {
        return this.mAlbum;
    }
    public void setMAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }
    public String getMArtist() {
        return this.mArtist;
    }
    public void setMArtist(String mArtist) {
        this.mArtist = mArtist;
    }
    public Long getMAlbumId() {
        return this.mAlbumId;
    }
    public void setMAlbumId(Long mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

//    public Album(Long id, String album, String artist) {
//        mId = id;
//        mAlbum = album;
//        mArtist = artist;
//
//        mUri = ContentUris.withAppendedId(
//                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,id);
//    }
//
//    public Uri getUri() {
//        return mUri;
//    }
//
//    public Long getId() {
//        return mId;
//    }
//
//    public String getAlbum() {
//        return mAlbum;
//    }
//
//    public String getArtist() {
//        return mArtist;
//    }
}
