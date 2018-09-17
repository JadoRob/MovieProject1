package org.udacity.android.movieproject1;

/* This adapter obtains the movie data and binds it to the grid */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity (tableName = "movies") class MovieData {

    @PrimaryKey
    int movieID;
    @ColumnInfo (name = "movie_title") String movieTitle;
    String movieImage;
    String synopsis;
    int userRating;
    String releaseDate;
    Boolean favorite = false;
    @Ignore final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
    @Ignore final String IMAGE_SIZE = "/w500";

     public MovieData(String movieTitle, String movieImage, String synopsis, int userRating,
                      int movieID, String releaseDate) {
         this.movieTitle = movieTitle;
         this.movieImage = BASE_IMAGE_URL + IMAGE_SIZE + movieImage;
         this.synopsis = synopsis;
         this.userRating = userRating;
         this.movieID = movieID;
         this.releaseDate = releaseDate;
     }

     void setFavorite(boolean favorite) {
         this.favorite = true;
     }
}
