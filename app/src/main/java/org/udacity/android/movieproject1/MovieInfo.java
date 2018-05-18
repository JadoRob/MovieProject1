package org.udacity.android.movieproject1;

/* This adapter obtains the movie data and binds it to the grid */

public class MovieInfo {

     String movieTitle;
     int moviePoster;

    public MovieInfo(String movieTitle, int moviePoster) {
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
    }
}
