package org.udacity.android.movieproject1;

import android.arch.lifecycle.ViewModel;

public class MovieViewModel extends ViewModel {

    MovieData movieData;

    public void setMovieData(MovieData movieData) {
        this.movieData = movieData;
    }

    public String getTitle() {
        return movieData.movieTitle;
    }

    public String getSynopsis() {
        return movieData.synopsis;
    }




}
