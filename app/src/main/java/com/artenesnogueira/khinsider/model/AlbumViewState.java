package com.artenesnogueira.khinsider.model;

import com.artenesnogueira.khinsider.api.model.Album;

public class AlbumViewState implements State {

    private final boolean isLoading;
    private final boolean hasError;
    private final String albumId;
    private final Album album;

    public static AlbumViewState makeLoadingState(String albumId) {
        return new AlbumViewState(true, false, albumId, null);
    }

    public static AlbumViewState makeErrorState(String albumId) {
        return new AlbumViewState(false, true, albumId, null);
    }

    public static AlbumViewState makeShowAlbumState(String albumId, Album album) {
        return new AlbumViewState(false, false, albumId, album);
    }

    private AlbumViewState(boolean isLoading, boolean hasError, String albumId, Album album) {
        this.isLoading = isLoading;
        this.hasError = hasError;
        this.albumId= albumId;
        this.album = album;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getAlbumId() {
        return albumId;
    }

    public Album getAlbum() {
        return album;
    }

}
