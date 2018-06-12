package org.udacity.android.movieproject1;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieFragment extends Fragment {

    private static final String TAG = MovieFragment.class.getSimpleName();
    public MovieArrayAdapter movieArrayAdapter;
    ArrayList<MovieData> movieData = new ArrayList<>();
    String movieTitle;
    String posterPath;
    String synopsis;
    int userRating;
    String releaseDate;



    class getMoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String jsonString;
            jsonString = NetworkUtil.getMovieInfo("popular");
            Log.i(TAG, jsonString); //Confirms the request for data was successful.
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);


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
                        releaseDate = movie.getString("release_date");
                        MovieData currentMovie = new MovieData(movieTitle, posterPath, synopsis,
                                userRating, releaseDate);
                        movieData.add(currentMovie);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                movieArrayAdapter.notifyDataSetChanged();

            } catch (Exception e) {

            }


        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        final MovieViewModel movieViewModel = ViewModelProviders.of(getActivity())
                .get(MovieViewModel.class);
        new getMoviesTask().execute();
        movieArrayAdapter = new MovieArrayAdapter(getActivity(), (movieData));

        //inflate the GridView and create a reference to the movieArrayAdapter, which contains
        //each movie image in an ImageView
        GridView gridView = rootView.findViewById(R.id.poster_grid);
        gridView.setAdapter(movieArrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), movieData.get(position).movieTitle,
                        Toast.LENGTH_SHORT).show();
                movieViewModel.setMovieData(movieData.get(position));
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}

