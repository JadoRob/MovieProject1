package org.udacity.android.movieproject1;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnItemClickListener,
        Serializable {

    private MovieViewModel mMovieViewModel;
    private String sortOrder;
    private MovieListAdapter adapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_SELECTION = "Selected Movie";
    public static final String EXTRA_ID = "Movie ID";
    public static final String SERIALIZE_MOVIE = "Pass Movie";
    public static final int SAVE_FAVORITE = 1;
    String toastMessage = "No favorites saved.";
    final int duration = Toast.LENGTH_SHORT;

    // api key stored in values/keys.xml
    public static String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiKey = getString(R.string.tmdb_api_key);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieListAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(MainActivity.this);

        //Retrieves the user saved preference for movie list sort order (default is popular)
        sortOrder = MoviePreferences.getMovieSortOrder(getApplication());

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        if (!internetConnection()) {
            toastMessage = "Internet data not available.";
            Toast.makeText(getApplicationContext(), toastMessage, Toast
                    .LENGTH_SHORT).show();
            mMovieViewModel.changeSortOrder("favorites");
        }

        //Observe the movie list and updates the AdapterView when user selects sort order.
        mMovieViewModel.getAllMovies().observe(this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable List<MovieData> movieList) {
                if (sortOrder.equals("favorites") && movieList.size() == 0) {
                    Toast.makeText(getApplication(), toastMessage, duration).show();
                }
                adapter.showMovies(movieList);
            }
        });

        }

    @Override
    public void onItemClick(int position) {
        MovieData currentMovie;
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        currentMovie = adapter.getMovie(position);
        detailsIntent.putExtra(EXTRA_SELECTION, position);
        detailsIntent.putExtra(EXTRA_ID, currentMovie.movieID);
        detailsIntent.putExtra(SERIALIZE_MOVIE, currentMovie);
        startActivityForResult(detailsIntent, SAVE_FAVORITE);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //mMovieViewModel updates movie list, and stores preference when user selects sort order.
        switch (item.getItemId()) {
            case R.id.popularity:
                sortOrder = "popular";
                mMovieViewModel.changeSortOrder(sortOrder);
                if (!internetConnection()) {
                    toastMessage = "Internet data not available.";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast
                            .LENGTH_SHORT).show();
                }
                break;
            case R.id.rating:
                sortOrder = "rating";
                mMovieViewModel.changeSortOrder(sortOrder);
                if (!internetConnection()) {
                    toastMessage = "Internet data not available.";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast
                            .LENGTH_SHORT).show();
                }
                break;
            case R.id.favorites:
                sortOrder = "favorites";
                //Observes the favorite list and displays a message if empty.
                mMovieViewModel.getFavoriteList().observe(this,
                        new Observer<List<MovieData>>() {
                    @Override
                    public void onChanged(@Nullable List<MovieData> movieData) {
                        if (movieData.size() == 0) {
                            toastMessage = "No favorites saved.";
                            Toast.makeText(getApplication(), toastMessage, duration).show();
                        }
                    }
                });
                mMovieViewModel.changeSortOrder(sortOrder);
                break;
            case R.id.delete:
                //Displays a dialog box to confirm deletion of saved favorites.
                deleteAllFavorites();
                break;
        }
        return true;
    }

    private void deleteAllFavorites() {
        DialogInterface.OnClickListener confirmDelete = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        message = "Favorites cleared.";
                        mMovieViewModel.deleteAll();
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        message = "Canceled";
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                }
            }
        };
        AlertDialog.Builder box = new AlertDialog.Builder(this);
        box.setMessage("Delete all favories?").setPositiveButton("Yes", confirmDelete)
                .setNegativeButton("No", confirmDelete).show();
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
