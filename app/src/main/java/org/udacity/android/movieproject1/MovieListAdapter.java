package org.udacity.android.movieproject1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final LayoutInflater mInflater;
    private List<MovieData> mMovies = new ArrayList<>();
    private static final String TAG = MovieListAdapter.class.getSimpleName();

    MovieListAdapter(Context context) {mInflater = LayoutInflater.from(context); }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView movieItemView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            movieItemView = itemView.findViewById(R.id.posterImageView);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (mMovies != null) {
            MovieData currentMovie = mMovies.get(position);
            Log.d(TAG, "Movie Image URL:" + currentMovie.movieImage );
            Picasso.get().load(currentMovie.movieImage).fit().centerInside()
                    .placeholder(R.drawable.loading)
                    .into(holder.movieItemView);
        }
    }

    @Override
    public int getItemCount() {

            return mMovies.size();

    }

    public void showMovies(List<MovieData> movies) {
        if (movies != null )
            this.mMovies = movies;
        notifyDataSetChanged();
    }
}
