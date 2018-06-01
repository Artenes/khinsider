package com.artenesnogueira.khinsider.tasks;

import android.os.AsyncTask;

import com.artenesnogueira.khinsider.api.JsoupHtmlDocumentReader;
import com.artenesnogueira.khinsider.api.KhinsiderRepository;
import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.model.SearchViewState;
import com.artenesnogueira.khinsider.model.View;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class SearchAlbunsTask extends AsyncTask<String, Void, SearchViewState> {

    private final KhinsiderRepository repository;
    private final WeakReference<View> viewReference;

    public SearchAlbunsTask(View view) {
        this.repository = new KhinsiderRepository(new JsoupHtmlDocumentReader());
        this.viewReference = new WeakReference<>(view);
    }

    @Override
    protected SearchViewState doInBackground(String... strings) {

        String searchQuery = strings[0];

        try {

            List<ResumedAlbum> albums = repository.searchAlbums(searchQuery);

            if (albums == null) {
                return SearchViewState.makeEmptyState(searchQuery);
            }

            return SearchViewState.makeShowAlbumsState(searchQuery, albums);

        } catch (IOException exception) {

            return SearchViewState.makeErrorState(searchQuery);

        }

    }

    @Override
    protected void onPostExecute(SearchViewState searchViewState) {

        View view = viewReference.get();

        if (view == null) {
            return;
        }

        view.render(searchViewState);

    }


}
