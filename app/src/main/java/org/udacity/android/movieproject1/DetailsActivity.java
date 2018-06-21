package org.udacity.android.movieproject1;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView movieImage = findViewById(R.id.movie_poster);
        TextView movieTitle = findViewById(R.id.movie_title);
        TextView releaseDate = findViewById(R.id.release_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date formatDate = new Date();

        try {
            formatDate = dateFormat.parse(MovieFragment.currentMovie.releaseDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            String date = simpleDateFormat.format(formatDate);
            releaseDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView synopsis = findViewById(R.id.synopsis);
        TextView userRating = findViewById(R.id.user_rating);
        Picasso.get().load(MovieFragment.currentMovie.movieImage).into(movieImage);
        movieTitle.setText(MovieFragment.currentMovie.movieTitle);
        synopsis.setText(MovieFragment.currentMovie.synopsis);
        userRating.setText("Average Rating: " + MovieFragment.currentMovie.userRating + "/10");




    }
}
