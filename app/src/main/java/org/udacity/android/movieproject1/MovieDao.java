package org.udacity.android.movieproject1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY position")
    LiveData<List<MovieData>> getAllMovies();

    @Query("SELECT * FROM movies WHERE position = :position")
    LiveData<MovieData> getMovie(int position);

//    @Query("SELECT * FROM movies WHERE favorite = 1 ORDER BY position")
//    LiveData<MovieData> getFavorite(int position);

    @Query("UPDATE movies SET favorite = :toggle WHERE position = :position")
    void setFavorite(int position, int toggle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovie(MovieData movieData);

    @Update
    void updateMovie(MovieData... movieData);

    @Delete
    void deleteMovie(MovieData movieData);

    @Query("DELETE FROM movies")
    void deleteAll();

}
