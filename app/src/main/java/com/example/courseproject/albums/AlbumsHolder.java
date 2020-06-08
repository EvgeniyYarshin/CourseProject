package com.example.courseproject.albums;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.model.Album;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlbumsHolder extends RecyclerView.ViewHolder {
    private TextView mTitle;
    private TextView mReleaseDate;

    public AlbumsHolder(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.tv_album_title);
        mReleaseDate = itemView.findViewById(R.id.tv_album_release_date);
    }

    public void bind(Album item, final AlbumsAdapter.OnItemClickListener listener) {
        mTitle.setText(item.getName());

        DateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH); //parsing date
        try {
            Date comment = compareFormat.parse(item.getReleaseDate());
            String mFormat = new SimpleDateFormat("yyyy-MM-dd").format(comment); //format date for yyyy-mm-dd
            mReleaseDate.setText(mFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (listener != null) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}

