package org.udacity.android.movieproject1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

public class MovieFragment extends Fragment {

    private MovieArrayAdapter movieArrayAdapter;

    MovieInfo[] movieInfos = {
            new MovieInfo("Movie 1", R.drawable.stellar1),
            new MovieInfo("Movie 2", R.drawable.stellar2),
            new MovieInfo("Movie 3", R.drawable.stellar3),
            new MovieInfo("Movie 4", R.drawable.stellar4),
            new MovieInfo("Movie 5", R.drawable.stellar5),
            new MovieInfo("Movie 6", R.drawable.stellar6),
            new MovieInfo("Movie 7", R.drawable.stellar7),
            new MovieInfo("Movie 8", R.drawable.stellar8),
            new MovieInfo("Movie 9", R.drawable.stellar9),
            new MovieInfo("Movie 10", R.drawable.stellar10),
            new MovieInfo("Movie 11", R.drawable.stellar11),
            new MovieInfo("Movie 12", R.drawable.stellar12),
            new MovieInfo("Movie 13", R.drawable.stellar13),
            new MovieInfo("Movie 14", R.drawable.stellar14),
            new MovieInfo("Movie 15", R.drawable.stellar15),
            new MovieInfo("Movie 16", R.drawable.stellar16),
            new MovieInfo("Movie 17", R.drawable.stellar17),
            new MovieInfo("Movie 18", R.drawable.stellar18),
            new MovieInfo("Movie 19", R.drawable.stellar19),
            new MovieInfo("Movie 20", R.drawable.stellar20),
    };

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        movieArrayAdapter = new MovieArrayAdapter(getActivity(), Arrays.asList(movieInfos));

        GridView gridView = rootView.findViewById(R.id.poster_grid);
        gridView.setAdapter(movieArrayAdapter);

        return rootView;
    }
}

