package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends AndroidViewModel implements OnTaskCompleted {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private LiveData<List<MovieData>> mMovieList;
    private LiveData<List<MovieData>> mFavoriteList;
    private MutableLiveData<List<MovieTrailers>> mMovieTrailersList;
    private MutableLiveData<List<UserReviews>> mUserReviewsList;
    private MovieRepository mMovieRepository;
    private MutableLiveData<String> mSortOrder;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = MovieRepository.getInstance(application);
        mMovieTrailersList = new MutableLiveData<>();
        mUserReviewsList = new MutableLiveData<>();
        mSortOrder = new MutableLiveData<>();
        mFavoriteList = mMovieRepository.getFavorites();

        //gets movie list from repository and updates the movie list live data whenever user
        // changes sort order
        mMovieList = Transformations.switchMap(mSortOrder, (newSortOrder) -> {
            return mMovieRepository.sort(newSortOrder);
        });

        mSortOrder.setValue(MoviePreferences.getMovieSortOrder(getApplication()));

    }

    public LiveData<List<MovieData>> getAllMovies() {
        return mMovieList;
    }

    public LiveData<List<MovieData>> getFavoriteList() {
        return mFavoriteList;
    }

    public void saveFavorite(MovieData movie) {
        mMovieRepository.saveFavorite(movie);
    }

    public void deleteFavorite(MovieData movie) {
        mMovieRepository.deleteFavorite(movie);
    }

    public void deleteAll() { mMovieRepository.deleteAll(); }


    public void changeSortOrder(String sortOrder) {
        MoviePreferences.setMovieSortOrder(getApplication(), sortOrder);
        mSortOrder.setValue(sortOrder);
    }

    public LiveData<List<MovieTrailers>> getTrailers() {
        return mMovieTrailersList;
    }

    public LiveData<List<UserReviews>> getReviews() {
        return mUserReviewsList;
    }

    //takes the selected movie ID and confirms if it is a favorite, then returns true/false
    public boolean check(int movieID) {
        boolean isFavorite = false;
        for (int i = 0; i < mFavoriteList.getValue().size(); i++) {
            if (movieID == mFavoriteList.getValue().get(i).movieID) {
                isFavorite = true;
            }
        }
        Log.i(TAG, "is favorite?: " + isFavorite);
        return isFavorite;
    }

    //interface used to process info after calling MovieAsyncTask
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
                mMovieTrailersList.setValue(list);
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
                }
                mUserReviewsList.setValue(list);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
