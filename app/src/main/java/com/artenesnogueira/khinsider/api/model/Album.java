package com.artenesnogueira.khinsider.api.model;

import java.util.List;

/**
 * An album that holds songs
 */
public class Album {

    private final String name;
    private final String numberOfFiles;
    private final String totalFilesize;
    private final String dateAdded;
    private final String releasedOn;
    private final String totalTime;
    private final List<String> images;
    private final List<Song> songs;

    public Album(String name, String numberOfFiles, String totalFilesize, String dateAdded, String releasedOn, String totalTime, List<String> images, List<Song> songs) {
        this.name = name;
        this.numberOfFiles = numberOfFiles;
        this.totalFilesize = totalFilesize;
        this.dateAdded = dateAdded;
        this.releasedOn = releasedOn;
        this.totalTime = totalTime;
        this.images = images;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public String getNumberOfFiles() {
        return numberOfFiles;
    }

    public String getTotalFilesize() {
        return totalFilesize;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getReleasedOn() {
        return releasedOn;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public List<String> getImages() {
        return images;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getCoverImage() {
        if (images.size() > 0) {
            return images.get(0);
        }
        return null;
    }

}