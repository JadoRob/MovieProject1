package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private LiveData<List<MovieData>> movieList;
    private LiveData<MovieData> movie;
    private MovieRepository mMovieRepository;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = new MovieRepository(application);
        movieList = mMovieRepository.getAllMovies();
    }

    public LiveData<List<MovieData>> getAllMovies() { return movieList; }

    public void saveMovie(MovieData movie) { mMovieRepository.saveMovie(movie);}

    public void saveFavorite(MovieData movie) {mMovieRepository.saveMovie(movie); }

    public void deleteAll() { mMovieRepository.deleteAll(); }

    public void updateMovies() {
        mMovieRepository.updateMovies();
    }

    public LiveData<MovieData> getMovie(int position) {
        movie = mMovieRepository.getMovie(position);
        return movie;
    }

    public void setMovieSortOrder(String sortOrder) {
        MoviePreferences.setMovieSortOrder(getApplication(), sortOrder);
    }
}
