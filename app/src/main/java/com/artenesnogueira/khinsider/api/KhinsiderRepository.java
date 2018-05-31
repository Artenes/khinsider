package com.artenesnogueira.khinsider.api;

import com.artenesnogueira.khinsider.api.model.Album;
import com.artenesnogueira.khinsider.api.model.File;
import com.artenesnogueira.khinsider.api.model.FileInfo;
import com.artenesnogueira.khinsider.api.model.Format;
import com.artenesnogueira.khinsider.api.model.Letter;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.api.model.Song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KhinsiderRepository {

    public List<ResumedAlbum> getAlbumsByLetter(Letter letter) throws IOException {

        String letterInString = letter == Letter.HASH ? "#" : letter.toString();

        Document page = Jsoup.connect(KhinsiderContract.getBrowseByLetterUrl(letterInString)).get();

        return parseResumedAlbumsFromList(page);

    }

    public List<ResumedAlbum> searchAlbums(String search) throws IOException {

        Document page = Jsoup.connect(KhinsiderContract.getSearchUrl(search)).get();

        return parseResumedAlbumsFromList(page);

    }

    public Album getAlbum(String id) throws IOException {

        Document page = Jsoup.connect(KhinsiderContract.getAlbumUrl(id)).get();

        Element echoTopic = page.getElementById("EchoTopic");
        Element infoParagraph = echoTopic.getElementsByTag("p").get(1);
        Elements details = infoParagraph.getElementsByTag("b");

        String name = echoTopic.getElementsByTag("h2").get(0).text();
        int numberOfFiles =  Integer.parseInt(details.get(1).text());
        String totalFilesize = details.get(2).text();
        String dateAdded = details.get(3).text();
        String releasedOn = "N/A";
        String totalTime = echoTopic.getElementById("songlist_footer").getElementsByTag("th").get(1).text();
        List<String> images = new ArrayList<>(0);
        List<Song> songs = new ArrayList<>(0);

        String releasedOnRegex = "(?<=Released on: ).*";
        Pattern pattern = Pattern.compile(releasedOnRegex);
        Matcher matcher = pattern.matcher(infoParagraph.text());
        if (matcher.find()) {
            releasedOn = matcher.group();
        }

        Elements imagesLinks = echoTopic.getElementsByTag("table").get(0).getElementsByTag("a");
        for (Element link : imagesLinks) {
            images.add(link.attr("href"));
        }

        Element songsTable = echoTopic.getElementById("songlist");
        int cdIndex = -1;
        int trackIndex = -1;
        int songNameIndex = -1;
        int timeIndex = -1;
        int mp3Index = -1;
        int flacIndex = -1;

        Elements headers = songsTable.getElementById("songlist_header").getElementsByTag("th");
        Elements rows = songsTable.getElementsByTag("tr");

        for (int index = 0; index < headers.size(); index++) {

            if (headers.get(index).text().equals("CD")) {
                cdIndex = index;
                continue;
            }

            if (headers.get(index).text().equals("Track")) {
                trackIndex = index;
                continue;
            }

            if (headers.get(index).text().equals("Song Name")) {
                songNameIndex = index;
                timeIndex = index + 1;
                continue;
            }

            if (headers.get(index).text().equals("MP3")) {
                mp3Index = timeIndex + 1;
                continue;
            }

            if (headers.get(index).text().equals("FLAC")) {
                flacIndex = mp3Index + 1;
            }

        }

        for (int index = 1; index < rows.size() - 1; index++) {

            Elements data = rows.get(index).getElementsByTag("td");
            String cd = cdIndex != -1 ? data.get(cdIndex).text() : "0";
            String track = trackIndex != -1 ? data.get(trackIndex).text() : "0";
            String songName = songNameIndex != -1 ? data.get(songNameIndex).text() : "0";
            String time = timeIndex != -1 ? data.get(timeIndex).text() : "0:00";
            String songId = songNameIndex != -1 ? data.get(songNameIndex).getElementsByTag("a").attr("href").substring(24) : "";

            Map<Format, FileInfo> files = new HashMap<>(0);

            if (mp3Index != -1) {
                files.put(Format.MP3, new FileInfo(Format.MP3, data.get(mp3Index).text()));
            }

            if (flacIndex != -1) {
                files.put(Format.FLAC, new FileInfo(Format.FLAC, data.get(flacIndex).text()));
            }

            songs.add(new Song(songId, cd, track, songName, time, files));

        }

        return new Album(name, numberOfFiles, totalFilesize, dateAdded, releasedOn, totalTime, images, songs);

    }

    public Map<Format, File> getFiles(String songId) throws IOException {

        Document page = Jsoup.connect(KhinsiderContract.getAlbumUrl(songId)).get();

        Element echoTopic = page.getElementById("EchoTopic");

        Map<Format, File> files = new HashMap<>(0);

        String sizeRegex = "(?<=\\().*(?=\\))";
        Pattern pattern = Pattern.compile(sizeRegex);

        Element mp3Paragraph = echoTopic.getElementsByTag("p").get(3);
        Element flacParagraph = echoTopic.getElementsByTag("p").get(4);

        if (mp3Paragraph.text().contains("MP3")) {

            String size = "0.00 MB";
            Matcher matcher = pattern.matcher(mp3Paragraph.text());
            if (matcher.find()) {
                size = matcher.group();
            }

            files.put(Format.MP3, new File(Format.MP3, size, mp3Paragraph.getElementsByTag("a").attr("href")));
        }

        if (flacParagraph.text().contains("FLAC")) {

            String size = "0.00 MB";
            Matcher matcher = pattern.matcher(flacParagraph.text());
            if (matcher.find()) {
                size = matcher.group();
            }

            files.put(Format.FLAC, new File(Format.FLAC, size, flacParagraph.getElementsByTag("a").attr("href")));
        }

        return files;

    }

    private List<ResumedAlbum> parseResumedAlbumsFromList(Document page) {

        List<ResumedAlbum> albums = new ArrayList<>(0);

        Elements paragraphs = page.getElementById("EchoTopic").getElementsByTag("p");

        if (paragraphs.size() < 2) {
            return albums;
        }

        Elements albumsLinks = paragraphs.get(1).getElementsByTag("a");

        for (Element link : albumsLinks) {

            String[] linkParts = link.attr("href").split("/");
            String id = linkParts[linkParts.length - 1];
            String title = link.text();

            albums.add(new ResumedAlbum(id, title));

        }

        return albums;

    }

}
