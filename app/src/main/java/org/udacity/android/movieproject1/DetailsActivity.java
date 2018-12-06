package org.udacity.android.movieproject1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.udacity.android.movieproject1.MainActivity.EXTRA_ID;
import static org.udacity.android.movieproject1.MainActivity.EXTRA_SELECTION;
import static org.udacity.android.movieproject1.MainActivity.SERIALIZE_MOVIE;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private MovieViewModel mMovieViewModel;
    int movieSelected;
    int movieID;
    boolean reviewsPopulated = false;
    boolean trailersPopulated = false;
    ImageButton favoriteButton;
    MovieData currentMovie;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final ImageView posterImage = findViewById(R.id.movie_poster);
        final TextView movieTitle = findViewById(R.id.movie_title);
        final TextView releaseDate = findViewById(R.id.release_date);
        final TextView synopsis = findViewById(R.id.synopsis);
        final TextView userRating = findViewById(R.id.user_rating);

        //receive user selected MovieData from MainActivity.
        Intent intent = getIntent();
        movieSelected = intent.getIntExtra(EXTRA_SELECTION, 0);
        movieID = intent.getIntExtra(EXTRA_ID, 0);
        currentMovie = (MovieData) intent.getSerializableExtra(SERIALIZE_MOVIE);

        //initialize ViewModel and query network for trailers and reviews for selected movie.
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.sendTrailerQuery(movieID);
        mMovieViewModel.sendReviewQuery(movieID);

        //user interaction to save/delete movie to favorite
        favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takes ID of selected movie and confirms if it is in the favorite list
                boolean isFavorite = mMovieViewModel.check(movieID);
                if (isFavorite) {
                    mMovieViewModel.deleteFavorite(currentMovie);
                }
                if (!isFavorite) {
                    mMovieViewModel.saveFavorite(currentMovie);
                }
            }
        });

        //populates movie data on screen
        loadUI(currentMovie, posterImage, movieTitle, synopsis, userRating,
                        releaseDate);

        mMovieViewModel.getTrailers().observe(this, new Observer<List<MovieTrailers>>() {
            @Override
            public void onChanged(@Nullable List<MovieTrailers> movieTrailers) {
                //prevent creating duplicate trailer buttons
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
                //prevent creating duplicate reviews
                if (!reviewsPopulated) {
                    for (int i = 0; i < userReviews.size(); i++) {
                        addUserReview(userReviews.get(i).getAuthor(),
                                userReviews.get(i).getReview());
                    }
                }
                reviewsPopulated = true;
            }
        });
    }

    private void loadUI(@Nullable MovieData movieData, ImageView posterImage, TextView movieTitle, TextView synopsis, TextView userRating, TextView releaseDate) {
        Picasso.get().load(movieData.movieImage)
                .placeholder(R.drawable.loading)
                .into(posterImage);
        movieTitle.setText(movieData.movieTitle);
        synopsis.setText(movieData.synopsis);
        userRating.setText("Average Rating: " + movieData.userRating + "/10");
        parseReleaseDate(releaseDate, movieData.releaseDate);

        //View model observes favorite list and updates the favorite button accordingly.
        mMovieViewModel.getFavoriteList().observe(this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable List<MovieData> movieData) {
                boolean isFavorite = mMovieViewModel.check(movieID);
                if (!isFavorite) {
                    favoriteButton.setImageResource(R.drawable.outline_grade_black_36dp);
                } else {
                    favoriteButton.setImageResource(R.drawable.baseline_grade_black_36dp);
                }
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
