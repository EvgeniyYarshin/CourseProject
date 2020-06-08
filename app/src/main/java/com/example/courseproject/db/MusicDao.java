package com.example.courseproject.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.courseproject.model.Album;
import com.example.courseproject.model.Comment;
import com.example.courseproject.model.Song;

import java.util.List;

@Dao
public abstract class MusicDao {

    //------------------------------------INSERT DATA-----------------------------------------------//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertComments(List<Comment> comments);

    //------------------------------------GET DATA BY PARAMETERS------------------------------------//
    @Query("SELECT * from album")
    public abstract List<Album> getAlbums();

    @Query("SELECT * FROM album WHERE id = :id")
    public abstract Album getParentById(long id);

    @Query("SELECT * FROM Song WHERE album_id = :id")
    public abstract List<Song> getSongsByAlbumId(int id);

    @Query("SELECT * FROM Comment WHERE album_id = :id")
    public abstract List<Comment> getCommentsByAlbumId(int id);

    public Album getAlbumByIdWithSongs(int id) {
        Album album = getParentById(id);
        List<Song> songs = getSongsByAlbumId(id);
        album.mSongs = songs;
        return album;
    }
}
