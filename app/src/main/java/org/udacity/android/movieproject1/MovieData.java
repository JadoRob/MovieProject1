package org.udacity.android.movieproject1;

/* This adapter obtains the movie data and binds it to the grid */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity (tableName = "movies") class MovieData implements Serializable {


    int position;
    @PrimaryKey
    int movieID;
    @ColumnInfo (name = "movie_title") String movieTitle;
    String movieImage;
    String synopsis;
    int userRating;
    String releaseDate;
    public Boolean favorite = false;

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