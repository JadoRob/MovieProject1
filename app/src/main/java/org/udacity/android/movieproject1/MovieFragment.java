package org.udacity.android.movieproject1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieFragment extends Fragment {

    private static final String TAG = MovieFragment.class.getSimpleName();
    private MovieArrayAdapter movieArrayAdapter;
    ArrayList<MovieData> movieData = new ArrayList<>();
    static ArrayList<MovieData> currentMovie;

    class getMoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String jsonString;
            jsonString = NetworkUtil.getMovieInfo("popular");
            Log.i(TAG, jsonString);
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray moviesArray = jsonObject.getJSONArray("results");
                MovieData currentMovie = new MovieData();
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);

                    try {
                        currentMovie.movieTitle = movie.getString("original_title");
                        currentMovie.movieImage = movie.getString("poster_path");
                        movieData.add(currentMovie);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (Exception e) {

            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        new getMoviesTask().execute();

        //Log.i(TAG, movieData.get(0).movieTitle); display the first movie title in the log for
        //testing, this crashes the app.

        movieArrayAdapter = new MovieArrayAdapter(getActivity(), (movieData));
        //inflate the GridView and create a reference to the movieArrayAdapter, which contains
        //each movie image in an ImageView
        GridView gridView = rootView.findViewById(R.id.poster_grid);
        gridView.setAdapter(movieArrayAdapter);

        return rootView;
    }
}

