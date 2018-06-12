package org.udacity.android.movieproject1;

/* This adapter obtains the movie data and binds it to the grid */

public class MovieData {

     String movieTitle;
     String movieImage;
     String synopsis;
     int userRating;
     String releaseDate;
     final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
     final String IMAGE_SIZE = "/w500";

     public MovieData() {

     }

     public MovieData(String movieTitle, String movieImage, String synopsis, int userRating,
                      String releaseDate) {
         this.movieTitle = movieTitle;
         this.movieImage = BASE_IMAGE_URL + IMAGE_SIZE + movieImage;
         this.synopsis = synopsis;
         this.userRating = userRating;
         this.releaseDate = releaseDate;


     }
}
