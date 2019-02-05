package com.example.musicplayer.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Lyric {

    @Id(autoincrement = true)
    private Long mId;
    private Long mMusicId;
    private String mLyric;
    private int mLyricTime;
    private int mDuration;
    @Generated(hash = 2537971)
    public Lyric(Long mId, Long mMusicId, String mLyric, int mLyricTime,
            int mDuration) {
        this.mId = mId;
        this.mMusicId = mMusicId;
        this.mLyric = mLyric;
        this.mLyricTime = mLyricTime;
        this.mDuration = mDuration;
    }
    @Generated(hash = 2083827090)
    public Lyric() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public Long getMMusicId() {
        return this.mMusicId;
    }
    public void setMMusicId(Long mMusicId) {
        this.mMusicId = mMusicId;
    }
    public String getMLyric() {
        return this.mLyric;
    }
    public void setMLyric(String mLyric) {
        this.mLyric = mLyric;
    }
    public int getMLyricTime() {
        return this.mLyricTime;
    }
    public void setMLyricTime(int mLyricTime) {
        this.mLyricTime = mLyricTime;
    }
    public int getMDuration() {
        return this.mDuration;
    }
    public void setMDuration(int mDuration) {
        this.mDuration = mDuration;
    }


}
