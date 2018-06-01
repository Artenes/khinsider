package com.artenesnogueira.khinsider.api;

import com.artenesnogueira.khinsider.api.model.Album;
import com.artenesnogueira.khinsider.api.model.File;
import com.artenesnogueira.khinsider.api.model.Format;
import com.artenesnogueira.khinsider.api.model.Letter;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.api.model.Song;
import com.artenesnogueira.khinsider.api.model.TopAlbums;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class KhinsiderRepositoryTest {

    private KhinsiderRepository repository;

    @Before
    public void setUp() {
        repository = new KhinsiderRepository(new JsoupHtmlDocumentReader());
    }

    @Test
    public void getAlbumsFromLetterA() throws IOException {

        List<ResumedAlbum> albums = repository.getAlbumsByLetter(Letter.A);

        //this number may vary in the future
        assertEquals(1110, albums.size());

        ResumedAlbum aBirdStory = albums.get(0);

        assertEquals("A Bird Story - Original Soundtrack", aBirdStory.getName());
        assertEquals("a-bird-story-original-soundtrack", aBirdStory.getId());

    }

    @Test
    public void getAlbumsFromHash() throws IOException {

        List<ResumedAlbum> albums = repository.getAlbumsByLetter(Letter.HASH);

        //this number may vary in the future
        assertEquals(105, albums.size());

        ResumedAlbum tenBillionWives = albums.get(0);

        assertEquals("10 billion wives (Android Game Music)", tenBillionWives.getName());
        assertEquals("10-billion-wives-android-game-music", tenBillionWives.getId());

    }

    @Test
    public void throwsExceptionWhenGetAlbumsWithNullLetter() {

        try {

            repository.getAlbumsByLetter(null);

        } catch (Exception exception) {

            assertTrue(exception instanceof NullPointerException);

        }

    }

    @Test
    public void searchKlonoaAlbum() throws IOException {

        List<ResumedAlbum> albums = repository.searchAlbums("klonoa");

        //this number may vary in the future
        assertEquals(10, albums.size());

        ResumedAlbum klonoaDoorToPhantomile = albums.get(9);

        assertEquals("Klonoa Of The Wind ~ Door To Phantomile", klonoaDoorToPhantomile.getName());
        assertEquals("klonoa-of-the-wind-door-to-phantomile", klonoaDoorToPhantomile.getId());

    }

    @Test
    public void getAlbumThatHasOneImageAndPlatform() throws IOException {

        Album album = repository.getAlbum("harvest-moon-island-of-happiness");

        assertEquals("Harvest Moon - Island of Happiness", album.getName());
        assertEquals("8", album.getNumberOfFiles());
        assertEquals("17.14 MB", album.getTotalFilesize());
        assertEquals("Mar 22nd, 2015", album.getDateAdded());
        assertEquals("Nintendo DS", album.getReleasedOn());
        assertEquals("9m 17s", album.getTotalTime());

        assertEquals(1, album.getImages().size());
        assertEquals("http://66.90.93.122/ost/harvest-moon-island-of-happiness/250px-Harvest_Moon_DS_Island_of_Happiness_box.jpg", album.getImages().get(0));

    }

    @Test
    public void getAlbumThatDoesntHaveImagesNorPlatform() throws IOException {

        Album album = repository.getAlbum("kaze-no-klonoa-moonlight-museum");

        assertEquals("Kaze no Klonoa - Moonlight Museum", album.getName());
        assertEquals("14", album.getNumberOfFiles());
        assertEquals("25.02 MB", album.getTotalFilesize());
        assertEquals("Nov 22nd, 2006", album.getDateAdded());
        assertEquals("N/A", album.getReleasedOn());
        assertEquals("19m 38s", album.getTotalTime());
        assertEquals(0, album.getImages().size());

    }

    @Test
    public void getAlbumThatHasMultipleImageAndPlatform() throws IOException {

        Album album = repository.getAlbum("a-girl-and-a-variant-of-the-circus-you-do-not-laugh-fantasy-left-training-game-seec-inc-android");

        assertEquals("A girl and a variant of the circus, you do not laugh [fantasy left training game] (SEEC Inc) (Android)", album.getName());
        assertEquals("5", album.getNumberOfFiles());
        assertEquals("8.21 MB", album.getTotalFilesize());
        assertEquals("Jul 21st, 2017", album.getDateAdded());
        assertEquals("Android", album.getReleasedOn());
        assertEquals("10m 16s", album.getTotalTime());

        assertEquals(6, album.getImages().size());
        assertEquals("http://66.90.93.122/ost/a-girl-and-a-variant-of-the-circus-you-do-not-laugh-fantasy-left-training-game-seec-inc-android/1.jpg", album.getImages().get(0));
        assertEquals("http://66.90.93.122/ost/a-girl-and-a-variant-of-the-circus-you-do-not-laugh-fantasy-left-training-game-seec-inc-android/6.png", album.getImages().get(5));

    }

    @Test
    public void getAlbumWithSongsThatHasOnlyTitleTimeAndMp3() throws IOException {

        Album album = repository.getAlbum("a-girl-and-a-variant-of-the-circus-you-do-not-laugh-fantasy-left-training-game-seec-inc-android");

        List<Song> songs = album.getSongs();

        assertEquals(5, songs.size());

        Song bgmHome = songs.get(0);
        assertEquals("bgm_home", bgmHome.getName());
        assertEquals("1:29", bgmHome.getTime());
        assertEquals("1.19 MB", bgmHome.getFiles().get(Format.MP3).getSize());
        assertEquals(Format.MP3, bgmHome.getFiles().get(Format.MP3).getFormat());
        assertEquals("a-girl-and-a-variant-of-the-circus-you-do-not-laugh-fantasy-left-training-game-seec-inc-android/bgm_home.mp3", bgmHome.getId());

    }

    @Test
    public void getAlbumWithSongsThatHasOnlyTrackTitleTimeAndMp3() throws IOException {

        Album album = repository.getAlbum("harvest-moon-island-of-happiness");

        List<Song> songs = album.getSongs();

        assertEquals(8, songs.size());

        Song titleTheme = songs.get(0);
        assertEquals("1.", titleTheme.getTrack());
        assertEquals("Harvest Moon IOH - Title Theme", titleTheme.getName());
        assertEquals("1:37", titleTheme.getTime());
        assertEquals("2.98 MB", titleTheme.getFiles().get(Format.MP3).getSize());
        assertEquals(Format.MP3, titleTheme.getFiles().get(Format.MP3).getFormat());
        assertEquals("harvest-moon-island-of-happiness/01%2520-%2520Harvest%2520Moon%2520IOH%2520-%2520Title%2520Theme.mp3", titleTheme.getId());

    }

    @Test
    public void getAlbumWithCompleteDetailedSongs() throws IOException {

        Album album = repository.getAlbum("klonoa-of-the-wind-door-to-phantomile");

        List<Song> songs = album.getSongs();

        assertEquals(68, songs.size());

        Song andIBeginToWonder = songs.get(0);
        assertEquals("1.", andIBeginToWonder.getTrack());
        assertEquals("1", andIBeginToWonder.getCd());
        assertEquals("And I Begin to Wonder", andIBeginToWonder.getName());
        assertEquals("0:25", andIBeginToWonder.getTime());
        assertEquals("1.38 MB", andIBeginToWonder.getFiles().get(Format.MP3).getSize());
        assertEquals("1.78 MB", andIBeginToWonder.getFiles().get(Format.FLAC).getSize());
        assertEquals(Format.MP3, andIBeginToWonder.getFiles().get(Format.MP3).getFormat());
        assertEquals(Format.FLAC, andIBeginToWonder.getFiles().get(Format.FLAC).getFormat());
        assertEquals("klonoa-of-the-wind-door-to-phantomile/1-01%2520And%2520I%2520Begin%2520to%2520Wonder.mp3", andIBeginToWonder.getId());

    }

    @Test
    public void getFilesToDownloadFromSongWithMp3AndFlac() throws IOException {

        Map<Format, File> files = repository.getFiles("klonoa-of-the-wind-door-to-phantomile/1-01%2520And%2520I%2520Begin%2520to%2520Wonder.mp3");

        assertNotNull(files.get(Format.MP3));
        assertNotNull(files.get(Format.FLAC));

        File mp3 = files.get(Format.MP3);
        File flac = files.get(Format.FLAC);

        assertEquals("1.38 MB", mp3.getSize());
        assertEquals(Format.MP3, mp3.getFormat());
        assertEquals("http://66.90.93.122/ost/klonoa-of-the-wind-door-to-phantomile/nmcchnzy/1-01%20And%20I%20Begin%20to%20Wonder.mp3", mp3.getUrl());

        assertEquals("1.78 MB", flac.getSize());
        assertEquals(Format.FLAC, flac.getFormat());
        assertEquals("http://66.90.93.122/ost/klonoa-of-the-wind-door-to-phantomile/nmcchnzy/1-01 And I Begin to Wonder.flac", flac.getUrl());

    }

    @Test
    public void getFilesToDownloadFromSongWithOnlyMp3() throws IOException {

        Map<Format, File> files = repository.getFiles("klonoa-heroes-gba/01-The%2520Windmill%2520Song.mp3");

        assertNotNull(files.get(Format.MP3));
        assertNull(files.get(Format.FLAC));

        File mp3 = files.get(Format.MP3);

        assertEquals("3.22 MB", mp3.getSize());
        assertEquals(Format.MP3, mp3.getFormat());
        assertEquals("http://66.90.93.122/ost/klonoa-heroes-gba/ocwpxrmj/01-The%20Windmill%20Song.mp3", mp3.getUrl());

    }

    @Test
    public void getPlatformsFromAlbumReleasedOnMultiplePlatforms() throws IOException {

        Album album = repository.getAlbum("pokemon-ten-years-of-pokemon");

        assertEquals("Game Boy Game Boy Advance Nintendo DS", album.getReleasedOn());

    }

    @Test
    public void returnNullIfAlbumIsNotFound() throws IOException {

        Album album;

        album = repository.getAlbum(null);
        assertNull(album);

        album = repository.getAlbum("");
        assertNull(album);

        album = repository.getAlbum("lasnflndkan");
        assertNull(album);

    }

    @Test
    public void returnNullIfSongIsNotFound() throws IOException {

        Map<Format, File> files;

        files = repository.getFiles(null);
        assertNull(files);

        files = repository.getFiles("");
        assertNull(files);

        files = repository.getFiles("alsdnaslns");
        assertNull(files);

    }

    @Test
    public void returnNullIfNoResultsWereFoundInSearch() throws IOException {

        List<ResumedAlbum> results;

        results = repository.searchAlbums(null);
        assertNull(results);

        results = repository.searchAlbums("");
        assertNull(results);

        results = repository.searchAlbums("alsdnaslns");
        assertNull(results);

    }

    @Test
    public void getTop40Albums() throws IOException, NullPointerException {

        List<ResumedAlbum> results = repository.getTopAlbums(TopAlbums.TOP_40);

        assertEquals(40, results.size());

        ResumedAlbum persona5 = results.get(0);

        assertEquals("persona-5", persona5.getId());
        assertEquals("Persona 5", persona5.getName());
        assertEquals("http://66.90.93.122/ost/persona-5/thumbs_small/Cover.jpg", persona5.getCover());

        //this one does not have a cover image
        ResumedAlbum donkeyKongCountry = results.get(20);

        assertEquals("donkey-kong-country-returns-original-sound-version", donkeyKongCountry.getId());
        assertEquals("Donkey Kong Country Returns - Original Sound Version", donkeyKongCountry.getName());
        assertNull(donkeyKongCountry.getCover());

    }

    @Test
    public void getTop100AllTimeAlbums() throws IOException, NullPointerException {

        List<ResumedAlbum> results = repository.getTopAlbums(TopAlbums.ALL_TIME);

        assertEquals(100, results.size());

        ResumedAlbum needForSpeed = results.get(0);

        assertEquals("need-for-speed-most-wanted", needForSpeed.getId());
        assertEquals("Need for Speed Most Wanted", needForSpeed.getName());
        assertEquals("http://66.90.93.122/ost/need-for-speed-most-wanted/thumbs_small/3407-okdgpilojw.jpg", needForSpeed.getCover());

    }

    @Test
    public void getTop100Last6MonthsAlbums() throws IOException, NullPointerException {

        List<ResumedAlbum> results = repository.getTopAlbums(TopAlbums.LAST_SIX_MOTHS);

        assertEquals(100, results.size());

        ResumedAlbum persona5 = results.get(0);

        assertEquals("persona-5", persona5.getId());
        assertEquals("Persona 5", persona5.getName());
        assertEquals("http://66.90.93.122/ost/persona-5/thumbs_small/Cover.jpg", persona5.getCover());

    }

    @Test
    public void getTop100NewlyAddedAlbums() throws IOException, NullPointerException {

        List<ResumedAlbum> results = repository.getTopAlbums(TopAlbums.NEWLY_ADDED);

        assertEquals(100, results.size());

        ResumedAlbum legendOfZelda = results.get(0);

        assertEquals("the-legend-of-zelda-breath-of-the-wild", legendOfZelda.getId());
        assertEquals("Legend of Zelda, The - Breath of the Wild", legendOfZelda.getName());
        assertEquals("http://66.90.93.122/ost/the-legend-of-zelda-breath-of-the-wild/thumbs_small/index.jpg", legendOfZelda.getCover());

    }

    @Test
    public void throwsExceptionWhenGetTopAlbumsWithNullType() {

        try {

            repository.getTopAlbums(null);

        } catch (Exception exception) {

            assertTrue(exception instanceof NullPointerException);

        }

    }

    //TODO change tests to use static HTML documents

}
