package com.example.musicplayer.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Music {
    @Id(autoincrement = true)
    private Long mId;
    private Long mMusicId;
    private String mTitle;
    private String mAlbum;
    private String mArtist;
    private Boolean mIsFavorite;
//    private Uri mUri;

    @Generated(hash = 1532862586)
    public Music(Long mId, Long mMusicId, String mTitle, String mAlbum,
            String mArtist, Boolean mIsFavorite) {
        this.mId = mId;
        this.mMusicId = mMusicId;
        this.mTitle = mTitle;
        this.mAlbum = mAlbum;
        this.mArtist = mArtist;
        this.mIsFavorite = mIsFavorite;
    }
    @Generated(hash = 1263212761)
    public Music() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public String getMTitle() {
        return this.mTitle;
    }
    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
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
    public Boolean getMIsFavorite() {
        return this.mIsFavorite;
    }
    public void setMIsFavorite(Boolean mIsFavorite) {
        this.mIsFavorite = mIsFavorite;
    }
    public Long getMMusicId() {
        return this.mMusicId;
    }
    public void setMMusicId(Long mMusicId) {
        this.mMusicId = mMusicId;
    }
    
//    public Music(Long id, String title, String album, String artist) {
//        mId = id;
//        mTitle = title;
//        mAlbum = album;
//        mArtist = artist;
//        mUri = ContentUris.withAppendedId(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
//
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
//    public String getTitle() {
//        return mTitle;
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
