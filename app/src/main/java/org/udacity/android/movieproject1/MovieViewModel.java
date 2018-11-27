package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends AndroidViewModel implements OnTaskCompleted {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private LiveData<List<MovieData>> movieList;
    private LiveData<MovieData> movie;
    private MutableLiveData<List<MovieTrailers>> movieTrailersList;
    private MutableLiveData<List<UserReviews>> userReviewsList;
    private MovieRepository mMovieRepository;



    public MovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = MovieRepository.getInstance(application);
        mMovieRepository.loadFavorites();
        movieList = mMovieRepository.getAllMovies();
        movieTrailersList = new MutableLiveData<>();
        userReviewsList = new MutableLiveData<>();
    }

    public LiveData<List<MovieData>> getAllMovies() { return movieList; }

    public void saveMovie(MovieData movie) { mMovieRepository.saveMovie(movie);}

    public void toggleFavorite(MovieData movie, int toggle) {
        mMovieRepository.toggleFavorite(movie, toggle);
    }

//    public void saveFavorite(int position) {
//        mMovieRepository.
//    }
    public void initializeFavorites() {

    }

    public void deleteAll() { mMovieRepository.deleteAll(); }

    public void updateMovies() {
        mMovieRepository.updateMovies();
    }

    public void updateFavorites() {
        mMovieRepository.updateFavorites();
    }

    public LiveData<MovieData> getMovie(int position) {
        movie = mMovieRepository.getMovie(position);
        return movie;
    }

    public LiveData<List<MovieTrailers>> getTrailers() {
        return movieTrailersList;
    }

    public LiveData<List<UserReviews>> getReviews() {
        return userReviewsList;
    }

    public void setMovieSortOrder(String sortOrder) {
        MoviePreferences.setMovieSortOrder(getApplication(), sortOrder);
    }

    public String getMovieSortOrder() {
        return MoviePreferences.getMovieSortOrder(getApplication());
    }

    public void checkFavorite() {

    }




    @Override
    public void onTaskCompleted(String s) {
        parseTrailerData(s);
        parseReviewData(s);
    }

    public void sendTrailerQuery(int movieID) {
        new MovieAsyncTask(this, "trailer", movieID).execute();
    }

    private void parseTrailerData(String jsonString) {
        if (jsonString != null && !jsonString.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray trailersArray = jsonObject.getJSONArray("results");
                List<MovieTrailers> list = new ArrayList<>();
                String key = null;
                String name = null;

                for (int i = 0; i < trailersArray.length(); i++) {
                    JSONObject trailer = trailersArray.getJSONObject(i);

                    try {
                        key = trailer.getString("key");
                        name = trailer.getString("name");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MovieTrailers movieTrailers = new MovieTrailers(name, key);
                    list.add(movieTrailers);
                }
                movieTrailersList.setValue(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendReviewQuery(int movieID) {
        new MovieAsyncTask(this, "reviews", movieID).execute();
    }

    private void parseReviewData(String jsonString) {
        if (jsonString != null && !jsonString.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray reviewsArray = jsonObject.getJSONArray("results");
                List<UserReviews> list = new ArrayList<>();
                String author;
                String content;

                for (int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject review = reviewsArray.getJSONObject(i);

                    author = review.getString("author");
                    content = review.getString("content");

                    UserReviews userReviews = new UserReviews(author, content);
                    list.add(userReviews);
                    //Log.i(TAG, "Author is: " + review.getString("author"));
                }
                userReviewsList.setValue(list);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
