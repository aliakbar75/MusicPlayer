package com.example.musicplayer.models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MusicLab {

    private static MusicLab instance;
    private ContentResolver mContentResolver;
    private MusicDao mMusicDao;

    private List<Music> mTracks = new ArrayList<>();
    private List<Album> mAlbums = new ArrayList<>();
    private List<Artist> mArtists = new ArrayList<>();

    public static MusicLab getInstance(Context context){
        if (instance == null)
            instance = new MusicLab(context);
        return instance;
    }

    public MusicLab(Context context) {

        context = context.getApplicationContext();

        MyDevOpenHelper myDevOpenHelper = new MyDevOpenHelper(context,"musics.db");
        Database database = myDevOpenHelper.getWritableDb();

        DaoSession daoSession = new DaoMaster(database).newSession();

        mMusicDao = daoSession.getMusicDao();

        mContentResolver = context.getContentResolver();
        Uri trackUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        getTracksList(trackUri);

        getAlbumsList(albumUri);

        getArtistsList(artistUri);

    }

    public void addMusic(Music music){
        mMusicDao.insert(music);
    }

    public void updateMusic(Music music){
        mMusicDao.update(music);
    }

    private void getArtistsList(Uri artistUri) {
        Cursor artistCursor = mContentResolver.query(artistUri,null,null,null,null);
        try {
            if (artistCursor.getCount() == 0)
                return;

            artistCursor.moveToFirst();
            while (!artistCursor.isAfterLast()) {
                Long id = artistCursor.getLong(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artist1 = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                Artist artist = new Artist();
                artist.setMArtistId(id);
                artist.setMArtist(artist1);
                mArtists.add(artist);
                artistCursor.moveToNext();
            }
        } finally {
            artistCursor.close();
        }

        Collections.sort(mArtists, new Comparator<Artist>(){
            public int compare(Artist a, Artist b){
                return a.getMArtist().compareTo(b.getMArtist());
            }
        });
    }

    private void getAlbumsList(Uri albumUri) {
        Cursor albumCursor = mContentResolver.query(albumUri,null,null,null,null);
        try {
            if (albumCursor.getCount() == 0)
                return;

            albumCursor.moveToFirst();
            while (!albumCursor.isAfterLast()) {
                Long id = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String album1 = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String artist = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                Album album = new Album();
                album.setMAlbumId(id);
                album.setMAlbum(album1);
                album.setMArtist(artist);
                mAlbums.add(album);
                albumCursor.moveToNext();
            }
        } finally {
            albumCursor.close();
        }

        Collections.sort(mAlbums, new Comparator<Album>(){
            public int compare(Album a, Album b){
                return a.getMAlbum().compareTo(b.getMAlbum());
            }
        });
    }

    private void getTracksList(Uri trackUri) {
        Cursor trackCursor = mContentResolver.query(trackUri,
                null,
                MediaStore.Audio.Media.IS_MUSIC,
                null,
                null);
        try {
            if (trackCursor.getCount() == 0)
                return;

            trackCursor.moveToFirst();
            while (!trackCursor.isAfterLast()) {
                Long id = trackCursor.getLong(trackCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                Music music = new Music();
                music.setMMusicId(id);
                music.setMTitle(title);
                music.setMAlbum(album);
                music.setMArtist(artist);
                music.setMIsFavorite(false);

                if (getTrackByMusicId(id) == null)
                    addMusic(music);
//                mTracks.add(music);
                trackCursor.moveToNext();
            }

        } finally {
            trackCursor.close();
        }

    }

    public List<Music> getTracks() {

        List<Music> tracks = new ArrayList<>();

//        if (name == null){
//
//        }


        tracks =  mMusicDao.queryBuilder()
                .list();

        Collections.sort(tracks, new Comparator<Music>(){
            public int compare(Music a, Music b){
                return a.getMTitle().compareTo(b.getMTitle());
            }
        });

        return tracks;


//        Uri trackUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//
//        getAlbumArtistTracksList(name, tracks, trackUri);
//        return tracks;

    }

    public List<Music> getTracksByAlbumArtistName(String name, boolean isAlbum){

        List<Music> tracks = new ArrayList<>();

        if (isAlbum){
            tracks = mMusicDao.queryBuilder()
                    .where(MusicDao.Properties.MAlbum.eq(name))
                    .list();
        }else {
            tracks = mMusicDao.queryBuilder()
                    .where(MusicDao.Properties.MArtist.eq(name))
                    .list();
        }

        Collections.sort(tracks, new Comparator<Music>(){
            public int compare(Music a, Music b){
                return a.getMTitle().compareTo(b.getMTitle());
            }
        });

        return tracks;
    }


//    private void getAlbumArtistTracksList(String name, List<Music> tracks, Uri trackUri) {
//        Cursor trackCursor = mContentResolver.query(trackUri,
//                null,
//                MediaStore.Audio.Media.IS_MUSIC +
//                        MediaStore.Audio.Media.ALBUM + " = '" + name + "' OR " +
//                        MediaStore.Audio.Media.ARTIST + " = '" + name + "' " ,
//                null,
//                null);
//        try {
//            if (trackCursor.getCount() == 0)
//                return;
//
//            trackCursor.moveToFirst();
//            while (!trackCursor.isAfterLast()) {
//                Long id = trackCursor.getLong(trackCursor.getColumnIndex(MediaStore.Audio.Media._ID));
//                String title = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                String album = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                String artist = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//
//                Music music = new Music();
//                music.setMMusicId(id);
//                music.setMTitle(title);
//                music.setMAlbum(album);
//                music.setMArtist(artist);
//                music.setMIsFavorite(false);
//                tracks.add(music);
//                trackCursor.moveToNext();
//            }
//
//            Collections.sort(tracks, new Comparator<Music>(){
//                public int compare(Music a, Music b){
//                    return a.getMTitle().compareTo(b.getMTitle());
//                }
//            });
//        } finally {
//            trackCursor.close();
//        }
//    }

    public List<Album> getAlbums() {
        return mAlbums;
    }

    public List<Artist> getArtists() {
        return mArtists;
    }

    public Music getTrack(Long id){
        for (Music music : getTracks()){
            if (music.getMId().equals(id)){
                return music;
            }
        }
        return null;
    }

    public Music getTrackByMusicId(Long id){
        for (Music music : getTracks()){
            if (music.getMMusicId().equals(id)){
                return music;
            }
        }
        return null;
    }
}
