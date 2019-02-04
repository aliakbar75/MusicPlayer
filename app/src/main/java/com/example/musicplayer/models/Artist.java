package com.example.musicplayer.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Artist {

    @Id(autoincrement = true)
    private Long mId;
    private Long mArtistId;
    private String mArtist;

    @Generated(hash = 288495906)
    public Artist(Long mId, Long mArtistId, String mArtist) {
        this.mId = mId;
        this.mArtistId = mArtistId;
        this.mArtist = mArtist;
    }
    @Generated(hash = 19829037)
    public Artist() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public String getMArtist() {
        return this.mArtist;
    }
    public void setMArtist(String mArtist) {
        this.mArtist = mArtist;
    }
    public Long getMArtistId() {
        return this.mArtistId;
    }
    public void setMArtistId(Long mArtistId) {
        this.mArtistId = mArtistId;
    }

//    public Artist(Long id, String artist) {
//        mId = id;
//        mArtist = artist;
//    }
//
//    public Long getId() {
//        return mId;
//    }
//
//    public String getArtist() {
//        return mArtist;
//    }
}
