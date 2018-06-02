package com.artenesnogueira.khinsider.model;

import com.artenesnogueira.khinsider.api.JsoupHtmlDocumentReader;
import com.artenesnogueira.khinsider.api.KhinsiderRepository;

/**
 * Helper to retrieve some dependencies used across the application
 */
public final class Dependecies {

    private static KhinsiderRepository repository;

    /**
     * Get the current album repository implementation
     * @return the album repository instance
     */
    public static KhinsiderRepository getRepository() {
        if (repository == null) {
            repository = new KhinsiderRepository(new JsoupHtmlDocumentReader());
        }
        return repository;
    }

}
