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
    String movieImageURL = buildImageURL();


    //The array movieData contains dummy data for testing. This will be replaced with JSON data
    //obtained from the internet
    MovieData[] movieData = {
            new MovieData("Movie 1", movieImageURL),
            new MovieData("Movie 2", movieImageURL),
            new MovieData("Movie 3", movieImageURL),
            new MovieData("Movie 4", movieImageURL),
            new MovieData("Movie 5", movieImageURL),
            new MovieData("Movie 6", movieImageURL),

    };

    public MovieFragment() {
    }

    private String buildImageURL() {
        String baseURL = "https://image.tmdb.org/t/p";
        String imageSize = "/w500";
        String imageURL = "/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg";
        return (baseURL + imageSize + imageURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        movieArrayAdapter = new MovieArrayAdapter(getActivity(), Arrays.asList(movieData));

        //inflate the GridView and create a reference to the movieArrayAdapter, which contains
        //each movie image in an ImageView
        GridView gridView = rootView.findViewById(R.id.poster_grid);
        gridView.setAdapter(movieArrayAdapter);

        return rootView;
    }
}

