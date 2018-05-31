package com.artenesnogueira.khinsider.api.model;

/**
 * Informations about a file. Does not provide url to download file
 */
public class FileInfo {

    private final Format format;
    private final String size;

    public FileInfo(Format format, String size) {
        this.format = format;
        this.size = size;
    }

    public Format getFormat() {
        return format;
    }

    public String getSize() {
        return size;
    }

}
