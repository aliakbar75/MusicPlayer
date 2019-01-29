package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.LinearLayout;

import com.example.musicplayer.models.Album;
import com.example.musicplayer.models.Artist;
import com.example.musicplayer.models.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MusicLab {

    private static MusicLab instance;
    ContentResolver mContentResolver;

    private List<Music> mTracks = new ArrayList<>();
    private List<Album> mAlbums = new ArrayList<>();
    private List<Artist> mArtists = new ArrayList<>();

    public static MusicLab getInstance(Context context){
        if (instance == null)
            instance = new MusicLab(context);
        return instance;
    }

    public MusicLab(Context context) {
        mContentResolver = context.getContentResolver();
        Uri trackUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor trackCursor = mContentResolver.query(trackUri,
                null,
                MediaStore.Audio.Media.DURATION + ">= 6000",
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
//                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
//                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, id);
                Music music = new Music(id,title,album,artist);
                mTracks.add(music);
                trackCursor.moveToNext();
            }



        } finally {
            trackCursor.close();
        }

        Collections.sort(mTracks, new Comparator<Music>(){
            public int compare(Music a, Music b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        Cursor albumCursor = mContentResolver.query(albumUri,null,null,null,null);
        try {
            if (albumCursor.getCount() == 0)
                return;

            albumCursor.moveToFirst();
            while (!albumCursor.isAfterLast()) {
                Long id = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String album1 = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String artist = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                Album album = new Album(id,album1,artist);
                mAlbums.add(album);
                albumCursor.moveToNext();
            }
        } finally {
            albumCursor.close();
        }

        Collections.sort(mAlbums, new Comparator<Album>(){
            public int compare(Album a, Album b){
                return a.getAlbum().compareTo(b.getAlbum());
            }
        });

        Cursor artistCursor = mContentResolver.query(artistUri,null,null,null,null);
        try {
            if (artistCursor.getCount() == 0)
                return;

            artistCursor.moveToFirst();
            while (!artistCursor.isAfterLast()) {
                Long id = artistCursor.getLong(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artist1 = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                Artist artist = new Artist(id,artist1);
                mArtists.add(artist);
                artistCursor.moveToNext();
            }
        } finally {
            artistCursor.close();
        }

        Collections.sort(mArtists, new Comparator<Artist>(){
            public int compare(Artist a, Artist b){
                return a.getArtist().compareTo(b.getArtist());
            }
        });

    }

    public List<Music> getTracks(String name) {

        List<Music> tracks = new ArrayList<>();

        if (name == null){
            return mTracks;
        }

        Uri trackUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor trackCursor = mContentResolver.query(trackUri,
                null,
                MediaStore.Audio.Media.DURATION + ">= 6000 AND " +
                        MediaStore.Audio.Media.ALBUM + " = '" + name + "' OR " +
                        MediaStore.Audio.Media.ARTIST + " = '" + name + "' " ,
                null,
                null);
        try {
            if (trackCursor.getCount() == 0)
                return null;

            trackCursor.moveToFirst();
            while (!trackCursor.isAfterLast()) {
                Long musicId = trackCursor.getLong(trackCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
//                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, id);
                Music music = new Music(musicId,title,album,artist);
                tracks.add(music);
                trackCursor.moveToNext();
            }

            Collections.sort(tracks, new Comparator<Music>(){
                public int compare(Music a, Music b){
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
        } finally {
            trackCursor.close();
        }
        return tracks;
    }

    public List<Album> getAlbums() {
        return mAlbums;
    }

    public List<Artist> getArtists() {
        return mArtists;
    }
}
