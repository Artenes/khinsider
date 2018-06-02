package com.artenesnogueira.khinsider.model;

import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.api.model.TopAlbums;

import java.util.List;

/**
 * State for view that display top albums
 */
public class TopAlbumsViewState implements State {

    private final boolean isLoading;
    private final boolean hasError;
    private final TopAlbums topAlbumType;
    private final List<ResumedAlbum> albums;

    public static TopAlbumsViewState makeLoadingState(TopAlbums type) {
        return new TopAlbumsViewState(true, false, type, null);
    }

    public static TopAlbumsViewState makeErrorState(TopAlbums type) {
        return new TopAlbumsViewState(false, true, type, null);
    }

    public static TopAlbumsViewState makeShowAlbumsState(TopAlbums type, List<ResumedAlbum> albums) {
        return new TopAlbumsViewState(false, false, type, albums);
    }

    private TopAlbumsViewState(boolean isLoading, boolean hasError, TopAlbums type, List<ResumedAlbum> albums) {
        this.isLoading = isLoading;
        this.hasError = hasError;
        this.topAlbumType = type;
        this.albums = albums;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasError() {
        return hasError;
    }

    public List<ResumedAlbum> getAlbums() {
        return albums;
    }

    public TopAlbums getTopAlbumType() {
        return topAlbumType;
    }

}
