package com.example.courseproject.db;

import com.example.courseproject.model.Album;
import com.example.courseproject.model.Comment;
import com.example.courseproject.model.Song;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Album.class, Song.class, Comment.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}
