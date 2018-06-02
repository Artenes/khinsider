package com.artenesnogueira.khinsider.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.adapter.SongsAdapter;
import com.artenesnogueira.khinsider.api.model.Album;
import com.artenesnogueira.khinsider.api.model.Song;
import com.artenesnogueira.khinsider.model.AlbumViewState;
import com.artenesnogueira.khinsider.model.State;
import com.artenesnogueira.khinsider.model.View;
import com.artenesnogueira.khinsider.viewmodel.AlbumViewModel;
import com.squareup.picasso.Picasso;

/**
 * Activity to display an album
 */
public class AlbumActivity extends AppCompatActivity implements View, SongsAdapter.OnSongClickListener {

    private static final String ALBUM_ID_KEY = "album_id";

    public static void start(Context context, String albumId) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(ALBUM_ID_KEY, albumId);
        context.startActivity(intent);
    }

    //@TODO: display scroll bar
    //@TODO: recyclerview does not retur to its previous position on orientation change
    //@TODO: layout for song does not fill the whole width of the screen (this gets noticed when you click in a item of the list)
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SongsAdapter mAdapter;
    private ImageView mAlbumCover;
    private CollapsingToolbarLayout mToolbar;

    private LinearLayout mErrorView;
    private LinearLayout mLoadingView;
    private CoordinatorLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        String id;

        Intent intent = getIntent();

        if (intent == null || !intent.hasExtra(ALBUM_ID_KEY)) {
            finish();
            return;
        }

        id = intent.getStringExtra(ALBUM_ID_KEY);

        if (id == null || id.isEmpty()) {
            finish();
            return;
        }

        mRecyclerView = findViewById(R.id.rv_songs_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SongsAdapter(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        mErrorView = findViewById(R.id.error_view);
        mLoadingView = findViewById(R.id.loading_view);
        mContentView = findViewById(R.id.cl_container);
        mAlbumCover = findViewById(R.id.iv_album_cover);
        mToolbar = findViewById(R.id.toolbar_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setTitle("");

        ViewModelProviders.of(this).get(AlbumViewModel.class)
                .getCurrentState(id).observe(this, new Observer<AlbumViewState>() {
            @Override
            public void onChanged(@Nullable AlbumViewState albumViewState) {
                render(albumViewState);
            }
        });

    }

    public void showLoading() {
        mLoadingView.setVisibility(android.view.View.VISIBLE);
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mContentView.setVisibility(android.view.View.INVISIBLE);
    }

    public void showError() {
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mErrorView.setVisibility(android.view.View.VISIBLE);
        mContentView.setVisibility(android.view.View.INVISIBLE);
    }

    public void showResults(Album album) {
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mContentView.setVisibility(android.view.View.VISIBLE);

        String image = album.getCoverImage();
        if (image != null) {
            Picasso.get().load(image).into(mAlbumCover);
        }

        mAdapter.swapData(album.getSongs());
        mToolbar.setTitle(album.getName());
    }

    @Override
    public void render(State state) {

        AlbumViewState currentState = (AlbumViewState) state;

        if (currentState.isLoading()) {
            showLoading();
            return;
        }

        if (currentState.hasError()) {
            showError();
            return;
        }

        if (currentState.getAlbum() != null) {
            showResults(currentState.getAlbum());
            return;
        }

        showError();

    }

    @Override
    public void onSongClicker(Song song) {
        //do something
    }

}
