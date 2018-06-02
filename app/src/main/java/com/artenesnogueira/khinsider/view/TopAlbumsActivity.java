package com.artenesnogueira.khinsider.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.adapter.TopAlbumsAdapter;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.api.model.TopAlbums;
import com.artenesnogueira.khinsider.model.State;
import com.artenesnogueira.khinsider.model.TopAlbumsViewState;
import com.artenesnogueira.khinsider.model.View;
import com.artenesnogueira.khinsider.viewmodel.TopAlbumsViewModel;

import java.util.List;

/**
 * Activity to display a top albums list
 */
public class TopAlbumsActivity extends AppCompatActivity implements View, TopAlbumsAdapter.OnAlbumClickListener {

    public static final String EXTRA_TYPE_TOP = "TYPE_TOP";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TopAlbumsAdapter mAdapter;
    private LinearLayout mErrorView;
    private LinearLayout mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_albums);

        mRecyclerView = findViewById(R.id.rv_albums);
        mErrorView = findViewById(R.id.error_view);
        mLoadingView = findViewById(R.id.loading_view);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TopAlbumsAdapter(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        TopAlbums type = TopAlbums.TOP_40;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TYPE_TOP)) {
            type = (TopAlbums) intent.getSerializableExtra(EXTRA_TYPE_TOP);
        }

        ViewModelProviders.of(this).get(TopAlbumsViewModel.class).getCurrentState(type)
                .observe(this, new Observer<TopAlbumsViewState>() {
            @Override
            public void onChanged(@Nullable TopAlbumsViewState topAlbumsViewState) {
                render(topAlbumsViewState);
            }
        });

    }

    public void showLoading() {
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mLoadingView.setVisibility(android.view.View.VISIBLE);
        mRecyclerView.setVisibility(android.view.View.INVISIBLE);
    }

    public void showError() {
        mErrorView.setVisibility(android.view.View.VISIBLE);
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mRecyclerView.setVisibility(android.view.View.INVISIBLE);
    }

    public void showAlbums(List<ResumedAlbum> albums) {
        mAdapter.swap(albums);
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mRecyclerView.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void render(State state) {

        TopAlbumsViewState viewState = (TopAlbumsViewState) state;

        if (viewState.isLoading()) {
            showLoading();
            return;
        }

        if (viewState.hasError()) {
            showError();
            return;
        }

        if (viewState.getAlbums() != null) {
            showAlbums(viewState.getAlbums());
            return;
        }

        showError();

    }

    @Override
    public void onAlbumClick(ResumedAlbum album) {

        AlbumActivity.start(this, album.getId());

    }

}
