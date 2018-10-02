package org.udacity.android.movieproject1;


import android.app.Activity;
import android.app.Application;
import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Movie;
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
    private String sortOrder;
    public static final String EXTRA_SELECTION = "Selected Movie";


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        final MovieListAdapter adapter = new MovieListAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(MainActivity.this);



        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable final List<MovieData> movieList) {
                adapter.showMovies(movieList);
                Toast.makeText(MainActivity.this, "onChange", Toast
                        .LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(EXTRA_SELECTION, position);
        startActivity(detailsIntent);
    }
}
