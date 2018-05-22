package org.udacity.android.movieproject1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieArrayAdapter extends ArrayAdapter<MovieData> {

    public MovieArrayAdapter(Activity context, List<MovieData> MovieList) {
        super(context, 0, MovieList);
    }

    //this method inflates the movie_poster layout to create a View for each data in the array.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieData movieData = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.movie_poster, parent, false);
        }

        ImageView posterView = convertView.findViewById(R.id.movie_poster);
        Picasso.get().load(movieData.movieImage).into(posterView);

        return convertView;
    }
}
