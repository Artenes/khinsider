package com.artenesnogueira.khinsider.api;

final class KhinsiderContract {

    private static final String BASE_URL = "https://downloads.khinsider.com";
    private static final String BROWSE_URL = BASE_URL + "/game-soundtracks/browse";
    private static final String SEARCH_URL = BASE_URL + "/search?search=";
    private static final String ALBUM_URL = BASE_URL + "/game-soundtracks/album";

    static String getBrowseByLetterUrl(String letter) {
        return BROWSE_URL + "/" + letter;
    }

    static String getSearchUrl(String search) {
        return SEARCH_URL + search;
    }

    static String getAlbumUrl(String id) {
        return ALBUM_URL + "/" + id;
    }

    static String appendPath(String path) {
        return BASE_URL + path;
    }

    private KhinsiderContract() {
    }

}
