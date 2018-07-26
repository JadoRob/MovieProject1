package org.udacity.android.movieproject1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

public class MovieFragment extends Fragment implements OnTaskCompleted {

    private static final String TAG = MovieFragment.class.getSimpleName();
    public static MovieData currentMovie;
    public MovieArrayAdapter movieArrayAdapter;
    ArrayList<MovieData> movieData = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        new GetMoviesTask(getActivity()).execute();
        movieArrayAdapter = new MovieArrayAdapter(getActivity(), movieData);
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        GridView gridView = rootView.findViewById(R.id.poster_grid);
        gridView.setAdapter(movieArrayAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentMovie = movieData.get(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
            }
        });
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
            case R.id.popularity:
                sortOrder = "popular";
                MoviePreferences.setMovieSortOrder(getActivity(), sortOrder);
                updateMovies();

                return true;
            case R.id.rating:
                sortOrder = "rating";
                MoviePreferences.setMovieSortOrder(getActivity(), sortOrder);
                updateMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTaskCompleted(String jsonString) {
        String movieTitle;
        String posterPath;
        String synopsis;
        int userRating;
        int movieID;
        String releaseDate;
        if (jsonString != null && !jsonString.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray moviesArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    try {
                        movieTitle = movie.getString("original_title");
                        posterPath = movie.getString("poster_path");
                        synopsis = movie.getString("overview");
                        userRating = movie.getInt("vote_average");
                        movieID = movie.getInt("id");
                        releaseDate = movie.getString("release_date");
                        currentMovie = new MovieData(movieTitle, posterPath, synopsis,
                                userRating, movieID, releaseDate);
                        movieData.add(currentMovie);
                        Log.i(TAG, "Movie: " + movieTitle + " ID: " + movieID);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        movieArrayAdapter.notifyDataSetChanged();
    }


    private void updateMovies() {
        movieData.clear();
        new GetMoviesTask(getActivity()).execute();
    }

    private boolean internetConnection() {
        boolean isConnected;
        Context context = getContext();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private String[] getTrailers() {
        String[] trailers = null;
        return trailers;
    }
}

