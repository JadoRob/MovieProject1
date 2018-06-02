package org.udacity.android.movieproject1;

/* This adapter obtains the movie data and binds it to the grid */

public class MovieData {

     String movieTitle;
     String movieImage;
     private final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
     private final String IMAGE_SIZE = "/w500";

     public MovieData() {
         this.movieTitle = null;
         this.movieImage = null;
     }

     public MovieData(String movieTitle, String movieImage) {
        this.movieTitle = movieTitle;
        this.movieImage = BASE_IMAGE_URL + IMAGE_SIZE + movieImage;
    }
}
