package com.example.courseproject.albums;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.courseproject.R;
import com.example.courseproject.model.Album;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsHolder> {

    @NonNull
    private final List<Album> mAlbums = new ArrayList<>();
    private final OnItemClickListener listener;

    public AlbumsAdapter(OnItemClickListener onClickListener) {
        listener = onClickListener;
    }

    @Override
    public AlbumsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.li_album, parent, false);
        return new AlbumsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsHolder holder, int position) {
        Album album = mAlbums.get(position);
        holder.bind(album, listener);
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public void addData(List<Album> data, boolean isRefreshed) {
        if (isRefreshed) {
            mAlbums.clear();
        }
        mAlbums.addAll(data);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Album item);
    }
}

