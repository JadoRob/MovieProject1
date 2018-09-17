package org.udacity.android.movieproject1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class AddMovieViewModel extends ViewModel {

    private LiveData<MovieData> movie;

    public AddMovieViewModel(MovieDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<MovieData> getMovie() {
        return movie;
    }
}
