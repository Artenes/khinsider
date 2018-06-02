package com.artenesnogueira.khinsider.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.api.model.Song;

import java.util.List;

/**
 * Adapter to display a list fo songs
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {

    private List<Song> songs;
    private OnSongClickListener mSongClickListener;

    public SongsAdapter(OnSongClickListener mSongClickListener) {
        this.mSongClickListener = mSongClickListener;
    }

    public void swapData(List<Song> albums) {
        this.songs = albums;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new SongsViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    class SongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView songName;
        private TextView songTime;

        public SongsViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.tv_name);
            songTime = itemView.findViewById(R.id.tv_time);
        }

        void bind(Song song) {
            songName.setText(song.getName());
            songTime.setText(song.getTime());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSongClickListener.onSongClicker(songs.get(getAdapterPosition()));
        }

    }

    public interface OnSongClickListener {

        void onSongClicker(Song song);

    }

}
