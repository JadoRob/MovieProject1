package org.udacity.android.movieproject1;

/* This adapter obtains the movie data and binds it to the grid */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity (tableName = "movies") class MovieData {

    @PrimaryKey
    int position;
    int movieID;
    @ColumnInfo (name = "movie_title") String movieTitle;
    String movieImage;
    String synopsis;
    int userRating;
    String releaseDate;
    Boolean favorite = false;

     public MovieData(String movieTitle, String movieImage, String synopsis, int userRating,
                      int movieID, String releaseDate, int position) {
         this.movieTitle = movieTitle;
         this.movieImage = movieImage;
         this.synopsis = synopsis;
         this.userRating = userRating;
         this.movieID = movieID;
         this.releaseDate = releaseDate;
         this.position = position;
     }

     void setFavorite(boolean favorite) {
         this.favorite = favorite;
     }
}
