package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository implements OnTaskCompleted {

    private static MovieRepository instance;
    private String query;
    private MovieDao mMovieDao;
    private FavoriteDao mFavoriteDao;
    private MutableLiveData<List<MovieData>> mMovieList;
    private List<MovieData> mFavoriteList;
    public LiveData<MovieData> movie;
    private static final Object LOCK = new Object();

    private Context mContext;
    private static final String TAG = MovieRepository.class.getSimpleName();

    MovieRepository(Application application) {

        mContext = application;
        MovieDatabase db = MovieDatabase.getDatabase(application);
        FavoriteDatabase fdb = FavoriteDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mFavoriteDao = fdb.favoriteDao();
        mMovieList = new MutableLiveData<>();
        query = MoviePreferences.getMovieSortOrder(mContext);
        new MovieAsyncTask(this, query).execute();
    }

    public static MovieRepository getInstance(Application application)
    {
        Log.i(TAG, "Retrieving the repository.");
        if (instance == null) {
            synchronized (LOCK) {
                instance = new MovieRepository(application);
                Log.i(TAG, "Creating the repository.");
            }
        }
        return(instance);
    }

    //this method returns all the movies saved in MovieDatabase
    LiveData<List<MovieData>> getAllMovies() {
        return mMovieList;
    }

    LiveData<MovieData> getMovie(int position) {
        //movie =  mMovieDao.getMovie(position);
        return movie;
    }

    void loadFavorites() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mFavoriteList = mFavoriteDao.getAllMovies();
            }
        });
        thread.start();
    }

    public void deleteAll() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mFavoriteDao.nukeTable();
            }
        });
        thread.start();
        Log.i(TAG, "Nuked!");
    }

    public void updateMovies() {
        query = MoviePreferences.getMovieSortOrder(mContext);
        new MovieAsyncTask(this, query).execute();
    }

    void updateFavorites() {
        if (mFavoriteList == null) {
            Toast.makeText(mContext, "No favorite movies saved.", Toast
                    .LENGTH_SHORT).show();
            return;
        }
        mMovieList.setValue(mFavoriteList);
    }

    public void toggleFavorite(final MovieData movie, final int toggle) {
        if (toggle == 1) {
            movie.favorite = true;
            saveFavorite(movie);
            return;
        }
        deleteFavorite(movie);
    }

    //method for saving mMovie to the favorites database
    public void saveMovie(final MovieData movie) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mMovieDao.saveMovie(movie);
            }
        });
        thread.start();
    }

    private void saveFavorite(final MovieData movie) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mFavoriteDao.saveMovie(movie);
                Log.i(TAG, movie.movieTitle + " saved as favorite!");
            }
        });
        thread.start();
    }

    private void deleteFavorite(final MovieData movie) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mFavoriteDao.deleteMovie(movie);
            }
        });
        thread.start();
    }

    //This interface is used to process the jsonString returned by MovieAsyncTask
    @Override
    public void onTaskCompleted(String s) {
        if (s == null) {
            Toast.makeText(mContext, "Internet data not available", Toast
                    .LENGTH_SHORT).show();
        } else {
            parseMovies(s);
        }
    }

//    private static class popularMoviesAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            return NetworkUtil.sendQueryURL(params[0]);
//        }
//        @Override
//        protected void onPostExecute(String jsonString) {
//            parseMovies(jsonString);
//        }
//    }
//
//    private static class ratedMoviesAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            return NetworkUtil.sendQueryURL(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String jsonString) {
//            parseMovies(jsonString);
//        }
//    }


    //Parses the jsonString
     private void parseMovies(String jsonString) {

        MovieData currentMovie;
        String movieTitle;
        String posterPath;
        String synopsis;
        int userRating;
        int movieID;
        String releaseDate;
        String baseImageUrl = "https://image.tmdb.org/t/p/w780" ;
        String nullMessage = "Unable to parse, JSON String is Null";
        String methodName = "From parseMovieDetails method: ";
        int position;
        Log.d(TAG, jsonString);

        List<MovieData> list = new ArrayList<>();

        if (jsonString != null && !jsonString.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray moviesArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);

                    movieTitle = movie.getString("original_title");
                    posterPath = baseImageUrl + movie.getString("poster_path");
                    synopsis = movie.getString("overview");
                    userRating = movie.getInt("vote_average");
                    movieID = movie.getInt("id");
                    releaseDate = movie.getString("release_date");
                    //favorite = checkFavorite(movieID);
                    position = i;
                    currentMovie = new MovieData(movieTitle, posterPath, synopsis,
                            userRating, movieID, releaseDate, position);
                    //currentMovie.setFavorite(favorite);
                    currentMovie.favorite = checkFavorite(currentMovie);
                    list.add(currentMovie);
                        //Log.d(TAG, "Movie Name: " + currentMovie.movieTitle);
                }

                Log.i(TAG, "Network Data retrieved!, Populating cache.");
                mMovieList.setValue(list);

                } catch (Exception e) {
                e.printStackTrace();
            }
        } else { Log.d(TAG, methodName + nullMessage); }
    }

    private boolean checkFavorite(MovieData movie) {
        for (int i = 0; i < mFavoriteList.size(); i++) {
            if (mFavoriteList.get(i).movieID == movie.movieID) {
                return true;
            }
        }
        return false;
    }

}
