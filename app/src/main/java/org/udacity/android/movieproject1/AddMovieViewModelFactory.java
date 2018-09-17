package org.udacity.android.movieproject1;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class AddMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final MovieDatabase mDb;
    private final int mMovieId;

    public AddMovieViewModelFactory(MovieDatabase database, int movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddMovieViewModel(mDb, mMovieId);
    }
}
