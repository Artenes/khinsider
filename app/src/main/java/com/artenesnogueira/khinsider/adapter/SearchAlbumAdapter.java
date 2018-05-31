package com.artenesnogueira.khinsider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;

import java.util.List;

public class SearchAlbumAdapter extends BaseAdapter {

    private List<ResumedAlbum> albums;
    private Context context;

    public SearchAlbumAdapter(Context context) {
        this.context = context;
    }

    public void swapData(List<ResumedAlbum> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return albums == null ? 0 : albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums == null ? null : albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.album_search_result, parent, false);
        }

        ((TextView) row.findViewById(R.id.tv_name)).setText(albums.get(position).getName());

        return row;

    }

}
