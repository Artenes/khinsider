package com.artenesnogueira.khinsider.api.model;

public class ResumedAlbum {

    private final String id;
    private final String name;

    public ResumedAlbum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
