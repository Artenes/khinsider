package com.artenesnogueira.khinsider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.api.model.Song;

import java.util.List;

public class SongsAdapter extends BaseAdapter {

    private List<Song> songs;
    private final Context context;

    public SongsAdapter(Context context) {
        this.context = context;
    }

    public void swapData(List<Song> albums) {
        this.songs = albums;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songs == null ? 0 : songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs == null ? null : songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.song_list_item, parent, false);
        }

        ((TextView) row.findViewById(R.id.tv_name)).setText(songs.get(position).getName());
        ((TextView) row.findViewById(R.id.tv_time)).setText(songs.get(position).getTime());

        return row;

    }

}
