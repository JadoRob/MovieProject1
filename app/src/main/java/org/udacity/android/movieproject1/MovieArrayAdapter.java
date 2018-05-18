package org.udacity.android.movieproject1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class MovieArrayAdapter extends ArrayAdapter<MovieInfo> {

    public MovieArrayAdapter(Activity context, List<MovieInfo> MovieList) {
        super(context, 0, MovieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieInfo movieInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.movie_poster, parent, false);
        }

        ImageView posterView = convertView.findViewById(R.id.movie_poster);
        posterView.setImageResource(movieInfo.moviePoster);

        return convertView;
    }
}
