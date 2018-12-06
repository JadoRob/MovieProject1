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
    private static final Object LOCK = new Object();
    protected String query;
    private FavoriteDao mFavoriteDao;
    private MutableLiveData<List<MovieData>> mMovieList;
    private MutableLiveData<List<MovieData>> mRatingList;
    private LiveData<List<MovieData>> mFavoriteList;


    private Context mContext;
    private static final String TAG = MovieRepository.class.getSimpleName();

    MovieRepository(Application application) {

        mContext = application;
        FavoriteDatabase fdb = FavoriteDatabase.getDatabase(application);
        mFavoriteDao = fdb.favoriteDao();
        mMovieList = new MutableLiveData<>();
        mRatingList = new MutableLiveData<>();
        mFavoriteList = mFavoriteDao.getAllMovies();
        query = MoviePreferences.getMovieSortOrder(mContext);
        //avoids performing a network query if user selects sort by favorite
        if (!query.equals("favorite")) {
            new MovieAsyncTask(this, query).execute();
        }

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

    //Calls network for data and returns the appropriate LiveData list depending on sort order
    // selected by the user
    public LiveData<List<MovieData>> sort(String sortOrder) {
        switch (sortOrder) {
            case "popular":
                new MovieAsyncTask(this, "popular").execute();
                return mMovieList;
            case "rating":
                new MovieAsyncTask(this, "rating").execute();
                return mRatingList;
            case "favorites":
                return mFavoriteList;
        }
        return null;
    }

    public LiveData<List<MovieData>> getFavorites() {
        return mFavoriteList;
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

    public void saveFavorite(final MovieData movie) {
        final String saved = "Movie saved as favorite.";
        final int duration = Toast.LENGTH_SHORT;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mFavoriteDao.saveMovie(movie);

            }
        });
        thread.start();
        Toast.makeText(mContext, saved, duration).show();
    }

    public void deleteFavorite(final MovieData movie) {
        final String deleted = "Movie Deleted from favorites.";
        final int duration = Toast.LENGTH_SHORT;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mFavoriteDao.deleteMovie(movie);

            }
        });
        thread.start();
        Toast.makeText(mContext, deleted, duration).show();
    }

    //This interface is used to process the jsonString returned by MovieAsyncTask
    @Override
    public void onTaskCompleted(String s) {
        if (s == null && (!MoviePreferences.getMovieSortOrder(mContext).equals("favorites"))) {
            Toast.makeText(mContext, "Internet data not available", Toast
                    .LENGTH_SHORT).show();
        } else {
            parseMovies(s);
        }
    }

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
                    //currentMovie.favorite = checkFavorite(currentMovie.movieID);
                    list.add(currentMovie);
                        //Log.d(TAG, "Movie Name: " + currentMovie.movieTitle);
                }

                Log.i(TAG, "Network Data retrieved!, Populating cache.");

                if (MoviePreferences.getMovieSortOrder(mContext).equals("popular")) {
                    mMovieList.setValue(list);
                } else if (MoviePreferences.getMovieSortOrder(mContext).equals("rating")) {
                    mRatingList.setValue(list);
                }


                } catch (Exception e) {
                e.printStackTrace();
            }
        } else { Log.d(TAG, methodName + nullMessage); }
    }

}
