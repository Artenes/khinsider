package com.artenesnogueira.khinsider.api.model;

import java.util.Map;

public class Song {

    private final String id;
    private final String cd;
    private final String track;
    private final String name;
    private final String time;
    private final Map<Format, FileInfo> files;

    public Song(String id, String cd, String track, String name, String time, Map<Format, FileInfo> files) {
        this.id = id;
        this.cd = cd;
        this.track = track;
        this.name = name;
        this.time = time;
        this.files = files;
    }

    public String getId() {
        return id;
    }

    public String getCd() {
        return cd;
    }

    public String getTrack() {
        return track;
    }

    public String getName() {
        return name;
    }

    public Map<Format, FileInfo> getFiles() {
        return files;
    }

    public String getTime() {
        return time;
    }

}
