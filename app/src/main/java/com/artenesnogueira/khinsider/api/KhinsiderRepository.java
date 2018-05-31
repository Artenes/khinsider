package com.artenesnogueira.khinsider.api;

import com.artenesnogueira.khinsider.api.model.Album;
import com.artenesnogueira.khinsider.api.model.File;
import com.artenesnogueira.khinsider.api.model.Format;
import com.artenesnogueira.khinsider.api.model.Letter;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A repository that holds songs from video games
 * It fetches all the data from downloads.khinsider.com
 * and uses fo htlm parser to read the data, since khinsider.com
 * does not provide an API
 */
public class KhinsiderRepository {

    private JsoupHtmlDocumentReader htmlDocumentReader;

    public KhinsiderRepository(JsoupHtmlDocumentReader htmlDocumentReader) {
        this.htmlDocumentReader = htmlDocumentReader;
    }

    /**
     * Gets a list of albums by letter
     *
     * @param letter the letter to search for
     * @return the list of albums
     * @throws IOException in case of connection or parser error
     */
    public List<ResumedAlbum> getAlbumsByLetter(Letter letter) throws IOException {

        String letterInString = letter == Letter.HASH ? "#" : letter.toString();

        Document page = htmlDocumentReader.readFromUrl(KhinsiderContract.getBrowseByLetterUrl(letterInString));

        try {

            return parseResumedAlbumsFromList(page);

        } catch (NullPointerException | IndexOutOfBoundsException exception) {

            throw new IOException("Error while parsing results", exception);

        }

    }

    /**
     * Search for albums with the given search parameter
     *
     * @param search the search parameter
     * @return the list of results. If none was found it returns an empty list
     * @throws IOException in case of connection or parser error
     */
    public List<ResumedAlbum> searchAlbums(String search) throws IOException {

        Document page = htmlDocumentReader.readFromUrl(KhinsiderContract.getSearchUrl(search));

        try {

            return parseResumedAlbumsFromList(page);

        } catch (NullPointerException | IndexOutOfBoundsException exception) {

            throw new IOException("Error while parsing results", exception);

        }

    }

    /**
     * Gets all the data of an album.
     *
     * @param id the id of the album. It is the slug of the album in the url.
     * @return the album
     * @throws IOException in case of connection or parser error
     */
    public Album getAlbum(String id) throws IOException {

        Document page = htmlDocumentReader.readFromUrl(KhinsiderContract.getAlbumUrl(id));

        AlbumPageParser albumPageParser = new AlbumPageParser(page);

        try {

            return albumPageParser.getAlbum();

        } catch (NullPointerException | IndexOutOfBoundsException exception) {

            throw new IOException("Error while parsing results", exception);

        }

    }

    /**
     * Get the available files to download for a song
     *
     * @param songId the id of the song, is the slug of the song in the donwload page
     *               plus the album name: album-name/music-name.mp3
     * @return a map with the available file formats to download
     * @throws IOException in case of connection or parser error
     */
    public Map<Format, File> getFiles(String songId) throws IOException {

        Document page = htmlDocumentReader.readFromUrl(KhinsiderContract.getAlbumUrl(songId));

        FilesPageParser filesPageParser = new FilesPageParser(page);

        try {

            return filesPageParser.getFiles();

        } catch (NullPointerException | IndexOutOfBoundsException exception) {

            throw new IOException("Error while parsing results", exception);

        }

    }

    /**
     * Gets the album names from a list of albums in a html page
     *
     * @param page the page to scrap
     * @return the list of found albums
     * @throws NullPointerException      if a html element is not found
     * @throws IndexOutOfBoundsException if a html element is no present in a list
     */
    private List<ResumedAlbum> parseResumedAlbumsFromList(Document page) throws IndexOutOfBoundsException, NullPointerException {

        List<ResumedAlbum> albums = new ArrayList<>(0);

        Elements paragraphs = page
                .getElementById(KhinsiderContract.DIV_ECHO_TOPIC)
                .getElementsByTag("p");

        //we expect that in a list of albums in the html page
        //should exists at least two paragraphs inside the EchoTopic div
        //the second one would hold the list of albums
        //if there is less paragraphs than this, just return an empty list
        //because probably no albums are rendered in the page
        if (paragraphs.size() < 2) {
            return albums;
        }

        //this gets the links that point out to the albums
        //each link has the name of the album and its url
        Elements albumsLinks = paragraphs.get(1).getElementsByTag("a");

        for (Element link : albumsLinks) {

            //we want only the name of the game as the id, not the relative url
            String[] linkParts = link.attr("href").split("/");
            //the last part of this url contains the name of the game
            String id = linkParts[linkParts.length - 1];

            String title = link.text();

            albums.add(new ResumedAlbum(id, title));

        }

        return albums;

    }

}
