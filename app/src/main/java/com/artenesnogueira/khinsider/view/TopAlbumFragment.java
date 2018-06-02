package com.artenesnogueira.khinsider.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.adapter.TopAlbumsAdapter;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.api.model.TopAlbums;
import com.artenesnogueira.khinsider.model.State;
import com.artenesnogueira.khinsider.model.TopAlbumsViewState;
import com.artenesnogueira.khinsider.viewmodel.TopAlbumsViewModel;

import java.util.List;

/**
 * Fragment used to display a list of top albums
 */
public class TopAlbumFragment extends Fragment
        implements TopAlbumsAdapter.OnAlbumClickListener, com.artenesnogueira.khinsider.model.View {

    public static final String KEY_TYPE_TOP = "TYPE_TOP";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TopAlbumsAdapter mAdapter;
    private LinearLayout mErrorView;
    private LinearLayout mLoadingView;

    public static Fragment make(TopAlbums type) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_TYPE_TOP, type);
        Fragment fragment = new TopAlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_top_albums, container, false);

        mRecyclerView = view.findViewById(R.id.rv_albums);
        mErrorView = view.findViewById(R.id.error_view);
        mLoadingView = view.findViewById(R.id.loading_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new TopAlbumsAdapter(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        TopAlbums type = TopAlbums.TOP_40;

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(KEY_TYPE_TOP)) {
            type = (TopAlbums) bundle.getSerializable(KEY_TYPE_TOP);
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

        AlbumActivity.start(getActivity(), album.getId());

    }

}