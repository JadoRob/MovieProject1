package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

//public class MovieViewModel extends AndroidViewModel {
//
//    private LiveData<List<MovieData>> movies;
//    private static final String TAG = MovieViewModel.class.getSimpleName();
//
//    public MovieViewModel(@NonNull Application application) {
//        super(application);
//        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
//        Log.i(TAG, "Retrieving movies from the DataBase");
//        movies = database.movieDao().loadMovies();
//    }
//
//    public LiveData<List<MovieData>> getMovies() {
//        return movies;
//    }
//}
