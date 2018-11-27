package org.udacity.android.movieproject1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.udacity.android.movieproject1.MainActivity.EXTRA_ID;
import static org.udacity.android.movieproject1.MainActivity.EXTRA_SELECTION;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private MovieViewModel mMovieViewModel;
    private MovieData currentMovie;
    int movieSelected;
    int movieID;
    ImageButton favoriteButton;
    boolean reviewsPopulated = false;
    boolean trailersPopulated = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final ImageView posterImage = findViewById(R.id.movie_poster);
        final TextView movieTitle = findViewById(R.id.movie_title);
        final TextView releaseDate = findViewById(R.id.release_date);
        final TextView synopsis = findViewById(R.id.synopsis);
        final TextView userRating = findViewById(R.id.user_rating);

        Intent intent = getIntent();
        movieSelected = intent.getIntExtra(EXTRA_SELECTION, 0);
        movieID = intent.getIntExtra(EXTRA_ID, 0);


        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.sendTrailerQuery(movieID);
        mMovieViewModel.sendReviewQuery(movieID);

        mMovieViewModel.getAllMovies().observe(this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable List<MovieData> movieData) {
                if (movieData != null) { currentMovie = movieData.get(movieSelected); }

                Picasso.get().load(movieData.get(movieSelected).movieImage)
                        .placeholder(R.drawable.loading)
                        .into(posterImage);
                movieTitle.setText(movieData.get(movieSelected).movieTitle);
                synopsis.setText(movieData.get(movieSelected).synopsis);
                userRating.setText("Average Rating: " + movieData.get(movieSelected)
                        .userRating + "/10");
                parseReleaseDate(releaseDate, movieData.get(movieSelected).releaseDate);
//                if (movieData.get(movieSelected).favorite) {
//                    Toast toast = Toast.makeText(getApplicationContext(), "is favorite",
//                            Toast.LENGTH_SHORT);
//                    toast.show();
//                } else {
//                    Toast toast = Toast.makeText(getApplicationContext(), "not favorite",
//                            Toast.LENGTH_SHORT);
//                    toast.show();
//                }

                if (!movieData.get(movieSelected).favorite) {
                    favoriteButton.setImageResource(R.drawable.outline_grade_black_36dp);
                } else if (movieData.get(movieSelected).favorite) {
                    favoriteButton.setImageResource(R.drawable.baseline_grade_black_36dp);
                }
            }
        });

//        mMovieViewModel.getMovie(movieSelected).observe(this, new Observer<MovieData>() {
//            @Override
//            public void onChanged(@Nullable MovieData movieData) {
//
//
//            }
//        });

        mMovieViewModel.getTrailers().observe(this, new Observer<List<MovieTrailers>>() {
            @Override
            public void onChanged(@Nullable List<MovieTrailers> movieTrailers) {
                if (!trailersPopulated) {
                    for (int i = 0; i < movieTrailers.size(); i++) {
                        addTrailerLink(movieTrailers.get(i).getTrailerName(),
                                movieTrailers.get(i).getYoutubeLink());
                    }
                }
                trailersPopulated = true;
            }
        });

        mMovieViewModel.getReviews().observe(this, new Observer<List<UserReviews>>() {
            @Override
            public void onChanged(@Nullable List<UserReviews> userReviews) {
                if (!reviewsPopulated) {
                    for (int i = 0; i < userReviews.size(); i++) {
                        addUserReview(userReviews.get(i).getAuthor(),
                                userReviews.get(i).getReview());
                    }
                }
                reviewsPopulated = true;
            }
        });

        favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting to true for testing
                int toggle = 0;

                if (!currentMovie.favorite) {
                    toggle = 1;
                    favoriteButton.setImageResource(R.drawable.baseline_grade_black_36dp);
                }
                if (currentMovie.favorite) {
                    toggle = 0;
                    favoriteButton.setImageResource(R.drawable.outline_grade_black_36dp);
                }
                mMovieViewModel.toggleFavorite(currentMovie, toggle);
            }
        });
    }

    private void parseReleaseDate(TextView releaseDate, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date formatDate;

        try {
            formatDate = dateFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            String returnDate = simpleDateFormat.format(formatDate);
            releaseDate.setText(returnDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addTrailerLink(final String name, final String link) {
        Button button = new Button(this);
        button.setText(name);
        LinearLayout trailerList = findViewById(R.id.trailerList);
        trailerList.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
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
