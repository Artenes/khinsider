package com.artenesnogueira.khinsider.api.model;

public class File extends FileInfo {

    private final String url;

    public File(Format format, String size, String url) {
        super(format, size);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
