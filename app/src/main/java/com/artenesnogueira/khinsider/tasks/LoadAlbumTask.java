package com.artenesnogueira.khinsider.tasks;

import android.os.AsyncTask;

import com.artenesnogueira.khinsider.api.JsoupHtmlDocumentReader;
import com.artenesnogueira.khinsider.api.KhinsiderRepository;
import com.artenesnogueira.khinsider.api.model.Album;
import com.artenesnogueira.khinsider.model.AlbumViewState;
import com.artenesnogueira.khinsider.model.View;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class LoadAlbumTask extends AsyncTask<String, Void, AlbumViewState> {

    private final KhinsiderRepository repository;
    private final WeakReference<View> viewReference;

    public LoadAlbumTask(View view) {
        this.repository = new KhinsiderRepository(new JsoupHtmlDocumentReader());
        this.viewReference = new WeakReference<>(view);
    }

    @Override
    protected AlbumViewState doInBackground(String... strings) {

        String albumId = strings[0];

        try {

            Album album = repository.getAlbum(albumId);
            return AlbumViewState.makeShowAlbumState(albumId, album);

        } catch (IOException exception) {

            return AlbumViewState.makeErrorState(albumId);

        }

    }

    @Override
    protected void onPostExecute(AlbumViewState searchViewState) {

        View view = viewReference.get();

        if (view == null) {
            return;
        }

        view.render(searchViewState);

    }


}
