package org.udacity.android.movieproject1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.udacity.android.movieproject1.MainActivity.EXTRA_SELECTION;

public class DetailsActivity extends AppCompatActivity implements OnTaskCompleted {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private MovieViewModel mMovieViewModel;
    private MovieData currentMovie;
    ImageButton favoriteButton;


    //private MovieDatabase mDb; //UI now uses MovieViewModel for retrieving data.


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final ImageView posterImage = findViewById(R.id.movie_poster);
        final TextView movieTitle = findViewById(R.id.movie_title);
        final TextView releaseDate = findViewById(R.id.release_date);
        final TextView synopsis = findViewById(R.id.synopsis);
        final TextView userRating = findViewById(R.id.user_rating);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date formatDate;

//        try {
//            formatDate = dateFormat.parse(currentMovie.releaseDate);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
//            String date = simpleDateFormat.format(formatDate);
//            releaseDate.setText(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        Intent intent = getIntent();
        final int movieSelected = intent.getIntExtra(EXTRA_SELECTION, 0);
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getMovie(movieSelected).observe(this, new Observer<MovieData>() {
            @Override
            public void onChanged(@Nullable MovieData movieData) {
                currentMovie = movieData;
                Picasso.get().load(movieData.movieImage)
                        .placeholder(R.drawable.loading)
                        .into(posterImage);

                movieTitle.setText(currentMovie.movieTitle);
                synopsis.setText(currentMovie.synopsis);
                userRating.setText("Average Rating: " + currentMovie.userRating + "/10");
            }
        });
    }








// Logic for favorite button being handled by LiveData
//        favoriteButton = findViewById(R.id.favoriteButton);
//        if (MovieFragment.movie.favorite) {
//            favoriteButton.setImageResource(R.drawable.baseline_grade_black_36dp);
//        }
//        favoriteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 updateFavorite();
//            }
//         });

//Activities no longer handle data processing, moving to MovieViewModel




//Movie Details to be passed from MovieViewModel




    //logic moving to MovieViewModel
//    private void updateFavorite() {
//        MovieData movieData = MovieFragment.movie;
//        CharSequence toastMessage = null;
//        int duration = Toast.LENGTH_SHORT;
//
//        if (!MovieFragment.movie.favorite) {
//            favoriteButton.setImageResource(R.drawable.baseline_grade_black_36dp);
//            MovieFragment.movie.favorite = true;
//            mDb.movieDao().saveMovie(movieData);
//            Log.d(TAG, movieData.movieTitle + ", favorite = " + movieData.favorite);
//            toastMessage = "Added to favorites";
//
//        } else if (MovieFragment.movie.favorite) {
//            favoriteButton.setImageResource(R.drawable.outline_grade_black_36dp);
//            MovieFragment.movie.favorite = false;
//            mDb.movieDao().deleteMovie(movieData);
//            Log.d(TAG, movieData.movieTitle + ", favorite = " + movieData.favorite);
//            toastMessage = "Removed from favorites";
//        }
//        Toast toast = Toast.makeText(context, toastMessage, duration);
//        toast.show();
//    }

    @Override
    public void onTaskCompleted(String jsonString) {
        Log.i(TAG, jsonString);
        //showTrailers(jsonString); logic moving to MovieViewModel
        //showReviews(jsonString); logic moving to MovieViewModel

    }

//logic moving to MovieViewModel
//    private void showTrailers(String jsonString) {
//        if (jsonString != null && !jsonString.equals("")) {
//            try {
//                JSONObject jsonObject = new JSONObject(jsonString);
//                JSONArray trailersArray = jsonObject.getJSONArray("results");
//                for (int i = 0; i < trailersArray.length(); i++) {
//                    JSONObject trailer = trailersArray.getJSONObject(i);
//                    String trailerKey;
//                    String trailerName;
//                    try {
//                        trailerKey = trailer.getString("key");
//                        trailerName = trailer.getString("name");
//                        addTrailerLink(trailerKey, trailerName);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    logic moving to MovieViewModel
//    private void showReviews(String jsonString) {
//        if (jsonString != null && !jsonString.equals("")) {
//            try {
//                JSONObject jsonObject = new JSONObject(jsonString);
//                JSONArray reviewsArray = jsonObject.getJSONArray("results");
//                for (int i = 0; i < reviewsArray.length(); i++) {
//                    JSONObject review = reviewsArray.getJSONObject(i);
//                    String author;
//                    String content;
//                    try {
//                        author = review.getString("author");
//                        content = review.getString("content");
//                        addUserReview(author, content);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void addTrailerLink(final String key, String name) {
        Button button = new Button(this);
        button.setText(name);
        LinearLayout trailerList = findViewById(R.id.trailerList);
        trailerList.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


    }

    private void addUserReview(String author, String content) {
        TextView reviewTextView = new TextView(this);
        reviewTextView.setText(content + "\n\n" + "Reviewed by: " + author);
        reviewTextView.setPadding(20, 20, 20, 200);
        LinearLayout reviewList = findViewById(R.id.reviewList);
        reviewList.addView(reviewTextView);
    }



}
