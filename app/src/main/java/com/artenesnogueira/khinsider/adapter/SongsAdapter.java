package com.artenesnogueira.khinsider.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.api.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private List<Song> songs = new ArrayList<>(0);
    private final SongClickListener mSongListener;
    private Song mCurrentSong;

    public SongsAdapter(SongClickListener songListener) {
        mSongListener = songListener;
    }

    public void swapData(List<Song> albums) {
        this.songs = albums;
        notifyDataSetChanged();
    }

    public void setCurrentSong(Song song) {
        if (mCurrentSong != null && mCurrentSong != song) {
            int prevPosition = songs.indexOf(mCurrentSong);
            notifyItemChanged(prevPosition);
        }
        int position = songs.indexOf(song);
        notifyItemChanged(position);
        mCurrentSong = song;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView name;
        private final TextView time;
        private final ImageView playOrPause;

        public SongViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            playOrPause = itemView.findViewById(R.id.iv_play_or_pause);
        }

        public void bind(Song song) {
            name.setText(song.getName());
            time.setText(song.getTime());

            if (song.isPlaying()) {
                playOrPause.setImageResource(android.R.drawable.ic_media_pause);
                itemView.setBackgroundColor(itemView.getContext().getColor(R.color.colorPrimaryDark));
            } else {
                playOrPause.setImageResource(android.R.drawable.ic_media_play);
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }

            name.setOnClickListener(this);
            name.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Song song = songs.get(position);
            mSongListener.onSongClicked(position, song);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            Song song = songs.get(position);
            mSongListener.onSongLongClicked(position, song);
            return true;
        }

    }

    public interface SongClickListener {

        void onSongClicked(int position, Song song);

        void onSongLongClicked(int position, Song song);

    }

}