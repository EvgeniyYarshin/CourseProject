package com.example.courseproject;

import android.app.Application;

import androidx.room.Room;

import com.example.courseproject.db.DataBase;

public class App extends Application {
    private DataBase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "music_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public DataBase getDatabase() {
        return mDatabase;
    }
}
