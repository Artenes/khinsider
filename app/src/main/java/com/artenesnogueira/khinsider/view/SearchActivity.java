package com.artenesnogueira.khinsider.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.adapter.SearchAlbumAdapter;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.model.SearchViewState;
import com.artenesnogueira.khinsider.model.State;
import com.artenesnogueira.khinsider.model.View;
import com.artenesnogueira.khinsider.tasks.SearchAlbunsTask;

public class SearchActivity extends AppCompatActivity implements View {

    private SearchViewState mCurrentState;

    private EditText mSearchEditText;
    private LinearLayout mLoadingView;
    private LinearLayout mErrorView;
    private LinearLayout mEmptyView;
    private LinearLayout mFirstView;
    private ListView mListView;
    private SearchAlbumAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchEditText = findViewById(R.id.et_search);
        mFirstView = findViewById(R.id.first_view);
        mLoadingView = findViewById(R.id.loading_view);
        mErrorView = findViewById(R.id.error_view);
        mEmptyView = findViewById(R.id.empty_view);
        mListView = findViewById(R.id.list_view);
        mAdapter = new SearchAlbumAdapter(this);

        mListView.setAdapter(mAdapter);
        mFirstView.setVisibility(android.view.View.VISIBLE);

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    render(SearchViewState.makeLoadingState(v.getText().toString()));
                    handled = true;
                }
                return handled;

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {

                ResumedAlbum album = (ResumedAlbum) mAdapter.getItem(position);
                AlbumActivity.start(SearchActivity.this, album.getId());

            }
        });

    }

    public void showLoading() {
        mLoadingView.setVisibility(android.view.View.VISIBLE);
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mEmptyView.setVisibility(android.view.View.INVISIBLE);
        mListView.setVisibility(android.view.View.INVISIBLE);
        mFirstView.setVisibility(android.view.View.INVISIBLE);
    }

    public void showError() {
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mErrorView.setVisibility(android.view.View.VISIBLE);
        mEmptyView.setVisibility(android.view.View.INVISIBLE);
        mListView.setVisibility(android.view.View.INVISIBLE);
        mFirstView.setVisibility(android.view.View.INVISIBLE);
    }

    public void showResults() {
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mEmptyView.setVisibility(android.view.View.INVISIBLE);
        mFirstView.setVisibility(android.view.View.INVISIBLE);
        mListView.setVisibility(android.view.View.VISIBLE);
        mAdapter.swapData(mCurrentState.getAlbums());
    }

    public void showNoResults() {
        mLoadingView.setVisibility(android.view.View.INVISIBLE);
        mErrorView.setVisibility(android.view.View.INVISIBLE);
        mEmptyView.setVisibility(android.view.View.VISIBLE);
        mListView.setVisibility(android.view.View.INVISIBLE);
        mFirstView.setVisibility(android.view.View.INVISIBLE);
    }

    @Override
    public void render(State state) {

        mCurrentState = (SearchViewState) state;

        if (mCurrentState.isLoading()) {
            showLoading();
            new SearchAlbunsTask(this).execute(mCurrentState.getSearchQuery());
            return;
        }

        if (mCurrentState.hasError()) {
            showError();
            return;
        }

        if (mCurrentState.isEmpty()) {
            showNoResults();
            return;
        }

        if (mCurrentState.getAlbums() != null) {
            showResults();
            return;
        }

        showError();

    }

}
