package org.udacity.android.movieproject1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {

    private static final String TAG = MovieFragment.class.getSimpleName();
    //public static MovieData movie; //to be replaced by LiveData (MovieViewModel)
    //public MovieArrayAdapter movieArrayAdapter; //to be replaced by RecyclerView;
    //List<MovieData> movieData = new ArrayList<>(); //to be replaced by LiveData (MovieViewModel)
    String queryString = "movies";
    private MovieViewModel mMovieViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Creates a connection to the MovieViewModel and UI (fragment)
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

//Old logic prior to adding Architecture Components (ViewModel, Room),
// also implementing RecyclerView
//        new MovieAsyncTask(this, queryString).execute();
//        movieArrayAdapter = new MovieArrayAdapter(getActivity(), movieData);
//        GridView gridView = rootView.findViewById(R.id.poster_grid);
//        gridView.setAdapter(movieArrayAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                movie = movieData.get(position);
//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sortOrder;
        if (!internetConnection()) {
            Log.i(TAG, "Unable to sort, No internet connection");
            return true;
        }
        switch (item.getItemId()) {
            //Removed onSaveInstanceState() logic for saving sort order and applied to the
            //MovieViewModel class
            case R.id.popularity:
                sortOrder = "popular";
                mMovieViewModel.setMovieSortOrder(sortOrder);
                //updateMovies(); this is removed, update will be handled by a call to the
                // RecyclerView
                return true;
            case R.id.rating:
                sortOrder = "rating";
                mMovieViewModel.setMovieSortOrder(sortOrder);

                //updateMovies(); this is removed, update will be handled by a call to the
                // RecyclerView
                return true;
            case R.id.favorites:
                sortOrder = "favorites";
                mMovieViewModel.setMovieSortOrder(sortOrder);
                //movieData = mDb.movieDao().getFavorites(); //UI should not have access to DB
                //movieArrayAdapter.notifyDataSetChanged(); //replacing with RecycleView
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //AsyncTask logic is handled by MovieRepository, which returns data to MovieViewModel to
    //parse the data.

//    @Override
//    public void onTaskCompleted(String jsonString) {
//        parseMovieDetails(jsonString);
//        movieArrayAdapter.notifyDataSetChanged();
//    }

//Logic removed, parsing will be handled by the MovieRepository prior to being passed to
//the ViewModel
//    private void parseMovieDetails(String jsonString) {
//        String movieTitle;
//        String posterPath;
//        String synopsis;
//        int userRating;
//        int movieID;
//        String releaseDate;
//        if (jsonString != null && !jsonString.equals("")) {
//            try {
//                JSONObject jsonObject = new JSONObject(jsonString);
//                JSONArray moviesArray = jsonObject.getJSONArray("results");
//                for (int i = 0; i < moviesArray.length(); i++) {
//                    JSONObject movie = moviesArray.getJSONObject(i);
//                    try {
//                        movieTitle = movie.getString("original_title");
//                        posterPath = movie.getString("poster_path");
//                        synopsis = movie.getString("overview");
//                        userRating = movie.getInt("vote_average");
//                        movieID = movie.getInt("id");
//                        releaseDate = movie.getString("release_date");
//                        movie = new MovieData(movieTitle, posterPath, synopsis,
//                                userRating, movieID, releaseDate);
//                        movieData.add(movie);
//                        Log.i(TAG, "Movie: " + movieTitle + " ID: " + movieID);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//This logic will be handled by MovieViewModel
//    private void updateMovies() {
//        movieData.clear();
//        new MovieAsyncTask(MovieFragment.this, queryString).execute();
//    }

    private boolean internetConnection() {
        boolean isConnected;
        Context context = getContext();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

//    private void setupViewModel() {
//        Log.i(TAG, "Retrieving saved movies from DataBase");
//        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
//        viewModel.getMovies().observe(this, new Observer<List<MovieData>>() {
//            @Override
//            public void onChanged(@Nullable List<MovieData> movieData) {
//                Log.d(TAG, "Updating movies from LiveData in ViewModel");
//            }
//        });
//    }
}

