package org.udacity.android.movieproject1;


import android.app.Activity;
import android.app.Application;
import android.app.FragmentTransaction;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnItemClickListener,
        Serializable {

    private MovieViewModel mMovieViewModel;
    MovieListAdapter adapter;
    public static final String EXTRA_SELECTION = "Selected Movie";
    public static final String EXTRA_ID = "Movie ID";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieData currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String sortOrder = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MovieListAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(MainActivity.this);

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        sortOrder = mMovieViewModel.getMovieSortOrder();

            mMovieViewModel.getAllMovies().observe(this, new Observer<List<MovieData>>() {
                @Override
                public void onChanged(@Nullable final List<MovieData> movieList) {
                    if (movieList != null) { adapter.showMovies(movieList); }
                    Log.i(TAG, "Onchanged triggered!");
                }
            });


    }

    @Override
    public void onItemClick(int position) {
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        currentMovie = adapter.getMovie(position);

        detailsIntent.putExtra(EXTRA_SELECTION, position);
        detailsIntent.putExtra(EXTRA_ID, currentMovie.movieID);
        startActivity(detailsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
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
                Toast.makeText(getApplicationContext(), sortOrder, Toast
                        .LENGTH_SHORT).show();
                mMovieViewModel.updateMovies();
                //updateMovies(); this is removed, update will be handled by a call to the
                // RecyclerView
                break;
            case R.id.rating:
                sortOrder = "rating";
                mMovieViewModel.setMovieSortOrder(sortOrder);
                Toast.makeText(getApplicationContext(), sortOrder, Toast
                        .LENGTH_SHORT).show();
                mMovieViewModel.updateMovies();
                //updateMovies(); this is removed, update will be handled by a call to the
                // RecyclerView
                break;
            case R.id.favorites:
                sortOrder = "favorites";
                mMovieViewModel.setMovieSortOrder(sortOrder);
                mMovieViewModel.updateFavorites();
                break;
        }
        return true;
    }

    private boolean internetConnection() {
        boolean isConnected;
        Context context = getApplicationContext();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
