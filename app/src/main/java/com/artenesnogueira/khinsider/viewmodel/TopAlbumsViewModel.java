package com.artenesnogueira.khinsider.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.artenesnogueira.khinsider.api.model.ResumedAlbum;
import com.artenesnogueira.khinsider.api.model.TopAlbums;
import com.artenesnogueira.khinsider.model.Dependecies;
import com.artenesnogueira.khinsider.model.TopAlbumsViewState;

import java.io.IOException;
import java.util.List;

/**
 * View model for top albums
 */
public class TopAlbumsViewModel extends ViewModel {

    private MutableLiveData<TopAlbumsViewState> mCurrentState;

    public LiveData<TopAlbumsViewState> getCurrentState(final TopAlbums type) {

        if (mCurrentState == null) {

            mCurrentState = new MutableLiveData<>();

            mCurrentState.setValue(TopAlbumsViewState.makeLoadingState(type));

            new LoadTopAlbumsTask(mCurrentState).execute(type);

        }

        return mCurrentState;

    }

    private static class LoadTopAlbumsTask extends AsyncTask<TopAlbums, Void, TopAlbumsViewState> {

        private MutableLiveData<TopAlbumsViewState> mMutableState;

        LoadTopAlbumsTask(MutableLiveData<TopAlbumsViewState> mMutableState) {
            this.mMutableState = mMutableState;
        }

        @Override
        protected TopAlbumsViewState doInBackground(TopAlbums... topAlbums) {

            TopAlbums type = topAlbums[0];

            try {

                List<ResumedAlbum> albums = Dependecies.getRepository().getTopAlbums(type);

                return TopAlbumsViewState.makeShowAlbumsState(type, albums);

            } catch (NullPointerException | IOException exception) {

                return TopAlbumsViewState.makeErrorState(type);

            }

        }

        @Override
        protected void onPostExecute(TopAlbumsViewState topAlbumsViewState) {

            mMutableState.setValue(topAlbumsViewState);

        }

    }

}
