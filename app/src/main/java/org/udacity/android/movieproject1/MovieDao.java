package org.udacity.android.movieproject1;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY movie_title")
    LiveData<List<MovieData>> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovie(MovieData movieData);

    @Delete
    void deleteMovie(MovieData movieData);

    @Query("DELETE FROM movies")
    void deleteAll();

    @Query("SELECT * FROM movies WHERE movieID = :id")
    LiveData<MovieData> loadMovieById(int id);


//    @Query("SELECT * FROM movies WHERE favorite = :true")
//    LiveData<List<MovieData>> getAllFavorites();
}
