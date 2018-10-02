package org.udacity.android.movieproject1;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class MovieRepository implements OnTaskCompleted {

    private String query;
    private MovieDao mMovieDao;
    private LiveData<List<MovieData>> mMovieList;

    private Context mContext;

    private static final String TAG = MovieRepository.class.getSimpleName();

    MovieRepository(Application application) {

        mContext = application;
        MovieDatabase db = MovieDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mMovieList = mMovieDao.getAllMovies();
        query = MoviePreferences.getMovieSortOrder(application);
        if (mMovieList == null)
        new MovieAsyncTask(this, query).execute();

    }

    //this method returns all the movies saved in MovieDatabase
    LiveData<List<MovieData>> getAllMovies() {
        return mMovieList;
    }

    LiveData<MovieData> getMovie(int position) {
        return mMovieDao.getMovie(position);
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

    public void deleteAll() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mMovieDao.deleteAll();
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
            int sizeOfMovieList = mMovieList.getValue().size();
            Log.d(TAG, "from onTaskCompleted: " + sizeOfMovieList);
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
        String baseImageUrl = "https://image.tmdb.org/t/p/w500" ;
        String nullMessage = "Unable to parse, JSON String is Null";
        String methodName = "From parseMovieDetails method: ";
        int position;
        Log.d(TAG, jsonString);

        if (jsonString != null && !jsonString.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray moviesArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    try {
                        movieTitle = movie.getString("original_title");
                        posterPath = baseImageUrl + movie.getString("poster_path");
                        synopsis = movie.getString("overview");
                        userRating = movie.getInt("vote_average");
                        movieID = movie.getInt("id");
                        releaseDate = movie.getString("release_date");
                        position = i;
                        currentMovie = new MovieData(movieTitle, posterPath, synopsis,
                                userRating, movieID, releaseDate, position);
                        saveMovie(currentMovie);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //checks to see if mMovieList is being populated
                }
                } catch (Exception e) {
                e.printStackTrace();
            }
        } else { Log.d(TAG, methodName + nullMessage); }

    }
}
