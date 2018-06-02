package com.artenesnogueira.khinsider.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.adapter.SongsAdapter;
import com.artenesnogueira.khinsider.api.model.Album;
import com.artenesnogueira.khinsider.model.AlbumViewState;
import com.artenesnogueira.khinsider.model.State;
import com.artenesnogueira.khinsider.model.View;
import com.artenesnogueira.khinsider.tasks.LoadAlbumTask;

public class AlbumActivity extends AppCompatActivity implements View {

    private static final String ALBUM_ID_KEY = "album_id";

    public static void start(Context context, String albumId) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(ALBUM_ID_KEY, albumId);
        context.startActivity(intent);
    }

    private TextView mAlbumNameTextView;
    private TextView mNumberOfFilesTextView;
    private TextView mTotalFileSizeTextView;
    private TextView mDateAddedTextView;
    private TextView mPlatformsTextView;
    private ListView mListView;

    private LinearLayout mErrorView;
    private LinearLayout mLoadingView;
    private ConstraintLayout mContentView;

    private SongsAdapter mAdapter;

    private AlbumViewState mCurrentState;

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

        mAlbumNameTextView = findViewById(R.id.tv_album_name);
        mNumberOfFilesTextView = findViewById(R.id.tv_number_files);
        mTotalFileSizeTextView = findViewById(R.id.tv_filesize);
        mDateAddedTextView = findViewById(R.id.tv_date_added);
        mPlatformsTextView = findViewById(R.id.tv_platforms);
        mListView = findViewById(R.id.list_view);

        mErrorView = findViewById(R.id.error_view);
        mLoadingView = findViewById(R.id.loading_view);
        mContentView = findViewById(R.id.content_view);

        mAdapter = new SongsAdapter(this);
        mListView.setAdapter(mAdapter);

        render(AlbumViewState.makeLoadingState(id));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");;

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

    public void showResults() {
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mContentView.setVisibility(android.view.View.VISIBLE);

        Album album = mCurrentState.getAlbum();

        mAlbumNameTextView.setText(album.getName());
        mNumberOfFilesTextView.setText(String.valueOf(album.getNumberOfFiles()));
        mTotalFileSizeTextView.setText(album.getTotalFilesize());
        mDateAddedTextView.setText(album.getDateAdded());
        mPlatformsTextView.setText(album.getReleasedOn());
        mAdapter.swapData(album.getSongs());
        getSupportActionBar().setTitle(album.getName());
    }

    @Override
    public void render(State state) {

        mCurrentState = (AlbumViewState) state;

        if (mCurrentState.isLoading()) {
            showLoading();
            new LoadAlbumTask(this).execute(mCurrentState.getAlbumId());
            return;
        }

        if (mCurrentState.hasError()) {
            showError();
            return;
        }

        if (mCurrentState.getAlbum() != null) {
            showResults();
            return;
        }

        showError();

    }

}
