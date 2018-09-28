package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private LiveData<List<MovieData>> movieList;
    private MovieRepository mMovieRepository;
    private String sortOrder;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = new MovieRepository(application);
        movieList = mMovieRepository.getAllMovies();
    }

    public LiveData<List<MovieData>> getAllMovies() { return movieList; }

    public void saveMovie(MovieData movie) { mMovieRepository.saveMovie(movie);}

    public void saveFavorite(MovieData movie) {mMovieRepository.saveMovie(movie); }

    public void deleteAll() { mMovieRepository.deleteAll(); }

    public void setMovieSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        MoviePreferences.setMovieSortOrder(getApplication(), sortOrder);
    }
}
