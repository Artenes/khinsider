package com.artenesnogueira.khinsider.model;

import com.artenesnogueira.khinsider.api.model.ResumedAlbum;

import java.util.List;

public class SearchViewState implements State {

    private final boolean isLoading;
    private final boolean hasError;
    private final String searchQuery;
    private final List<ResumedAlbum> albums;

    public static SearchViewState makeLoadingState(String searchQuery) {
        return new SearchViewState(true, false, searchQuery, null);
    }

    public static SearchViewState makeErrorState(String searchQuery) {
        return new SearchViewState(false, true, searchQuery, null);
    }

    public static SearchViewState makeShowAlbumsState(String searchQuery, List<ResumedAlbum> albums) {
        return new SearchViewState(false, false, searchQuery, albums);
    }

    private SearchViewState(boolean isLoading, boolean hasError, String searchQuery, List<ResumedAlbum> albums) {
        this.isLoading = isLoading;
        this.hasError = hasError;
        this.searchQuery = searchQuery;
        this.albums = albums;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public List<ResumedAlbum> getAlbums() {
        return albums;
    }

}
