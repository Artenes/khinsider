package com.artenesnogueira.khinsider.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to display top albums
 */
public class TopAlbumsAdapter extends RecyclerView.Adapter<TopAlbumsAdapter.TopAlbumViewHolder> {

    private List<ResumedAlbum> albums;
    private final OnAlbumClickListener albumClickListener;

    public TopAlbumsAdapter(OnAlbumClickListener albumClickListener) {
        this.albumClickListener = albumClickListener;
    }

    public void swap(List<ResumedAlbum> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.top_album_item, parent, false);

        return new TopAlbumViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TopAlbumViewHolder holder, int position) {
        ResumedAlbum album = albums.get(position);
        holder.bind(album);
    }

    @Override
    public int getItemCount() {

        if (albums == null) {
            return 0;
        }

        return albums.size();

    }

    class TopAlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        ImageView albumCover;
        TextView albumName;

        TopAlbumViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_contents);
            albumCover = itemView.findViewById(R.id.iv_album_cover);
            albumName = itemView.findViewById(R.id.tv_album_name);
        }

        void bind(ResumedAlbum album) {
            //TODO put placeholder for albums without image
            Picasso.get().load(album.getCover()).into(albumCover);
            albumName.setText(album.getName());
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            albumClickListener.onAlbumClick(albums.get(getAdapterPosition()));
        }

    }

    public interface OnAlbumClickListener {

        void onAlbumClick(ResumedAlbum album);

    }

}
