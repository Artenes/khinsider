package com.artenesnogueira.khinsider.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.artenesnogueira.khinsider.api.model.Album;
import com.artenesnogueira.khinsider.model.AlbumViewState;
import com.artenesnogueira.khinsider.model.Dependecies;

import java.io.IOException;

/**
 * View model for an album
 */
public class AlbumViewModel extends ViewModel {

    private MutableLiveData<AlbumViewState> mCurrentState;

    public LiveData<AlbumViewState> getCurrentState(final String id) {

        if (mCurrentState == null) {

            mCurrentState = new MutableLiveData<>();

            mCurrentState.setValue(AlbumViewState.makeLoadingState(id));

            new LoadTopAlbumsTask(mCurrentState).execute(id);

        }

        return mCurrentState;

    }

    private static class LoadTopAlbumsTask extends AsyncTask<String, Void, AlbumViewState> {

        private MutableLiveData<AlbumViewState> mMutableState;

        LoadTopAlbumsTask(MutableLiveData<AlbumViewState> mMutableState) {
            this.mMutableState = mMutableState;
        }

        @Override
        protected AlbumViewState doInBackground(String... ids) {

            String id = ids[0];

            try {

                Album album = Dependecies.getRepository().getAlbum(id);

                return AlbumViewState.makeShowAlbumState(id, album);

            } catch (NullPointerException | IOException exception) {

                return AlbumViewState.makeErrorState(id);

            }

        }

        @Override
        protected void onPostExecute(AlbumViewState topAlbumsViewState) {

            mMutableState.setValue(topAlbumsViewState);

        }

    }

}
