package org.udacity.android.movieproject1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM movies ORDER BY movie_title")
    LiveData<List<MovieData>> getAllMovies();

    @Query("SELECT * FROM movies WHERE position = :movieID")
    LiveData<MovieData> getMovie(int movieID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovie(MovieData movieData);

    @Delete
    void deleteMovie(MovieData movieData);

    @Query("DELETE FROM movies")
    void nukeTable();

}
