package com.example.courseproject.album;;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.model.Song;

public class SongsHolder extends RecyclerView.ViewHolder {
    private TextView mTitle;
    private TextView mDuration;

    public SongsHolder(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.tv_song_title);
        mDuration = itemView.findViewById(R.id.tv_song_duration);
    }

    public void bind(Song item) {
        mTitle.setText(item.getName());
        mDuration.setText(item.getDuration());
    }
}
