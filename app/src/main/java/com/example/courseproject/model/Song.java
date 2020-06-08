package com.example.courseproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity (
        foreignKeys = @ForeignKey(
                entity = Album.class, parentColumns = "id", childColumns = "album_id",
                onDelete = CASCADE),
        indices = {@Index("album_id")}
)
public class Song implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String mName;

    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    private String mDuration;

    @SerializedName("album_id")
    @ColumnInfo(name = "album_id")
    public long albumId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }
}
