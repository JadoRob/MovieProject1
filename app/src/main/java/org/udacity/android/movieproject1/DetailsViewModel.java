package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public class DetailsViewModel extends AndroidViewModel {

    private static final String TAG = DetailsViewModel.class.getSimpleName();
    private LiveData<MovieData> movie;
    private MovieRepository mMovieRepository;

    public DetailsViewModel(Application application) {
        super(application);
        mMovieRepository = new MovieRepository(application);

    }

    public LiveData<MovieData> getMovie(int position) {
        movie = mMovieRepository.getMovie(position);
        return movie;
    }

}
