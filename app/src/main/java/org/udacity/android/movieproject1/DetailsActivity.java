package org.udacity.android.movieproject1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity implements OnTaskCompleted {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private Context context = DetailsActivity.this;
    ImageButton favoriteButton;
    private MovieDatabase mDb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDb = MovieDatabase.getDatabase(getApplicationContext());

        //new MovieAsyncTask(DetailsActivity.this, context, "trailers").execute();
        //new MovieAsyncTask(DetailsActivity.this, context, "reviews").execute();
        //retrieving data is now handled by the MovieRepository

        ImageView movieImage = findViewById(R.id.movie_poster);
        TextView movieTitle = findViewById(R.id.movie_title);
        TextView releaseDate = findViewById(R.id.release_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date formatDate = new Date();

// Logic for favorite button being handled by LiveData
//        favoriteButton = findViewById(R.id.favoriteButton);
//        if (MovieFragment.currentMovie.favorite) {
//            favoriteButton.setImageResource(R.drawable.baseline_grade_black_36dp);
//        }
//        favoriteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 updateFavorite();
//            }
//         });

//Activities no longer handle data processing, moving to MovieViewModel
//        try {
//            formatDate = dateFormat.parse(MovieFragment.currentMovie.releaseDate);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
//            String date = simpleDateFormat.format(formatDate);
//            releaseDate.setText(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        TextView synopsis = findViewById(R.id.synopsis);
        TextView userRating = findViewById(R.id.user_rating);

//Movie Details to be passed from MovieViewModel
//        Picasso.get().load(MovieFragment.currentMovie.movieImage).into(movieImage);
//        movieTitle.setText(MovieFragment.currentMovie.movieTitle);
//        synopsis.setText(MovieFragment.currentMovie.synopsis);
//        userRating.setText("Average Rating: " + MovieFragment.currentMovie.userRating + "/10");
    }

    //logic moving to MovieViewModel
//    private void updateFavorite() {
//        MovieData movieData = MovieFragment.currentMovie;
//        CharSequence toastMessage = null;
//        int duration = Toast.LENGTH_SHORT;
//
//        if (!MovieFragment.currentMovie.favorite) {
//            favoriteButton.setImageResource(R.drawable.baseline_grade_black_36dp);
//            MovieFragment.currentMovie.favorite = true;
//            mDb.movieDao().saveMovie(movieData);
//            Log.d(TAG, movieData.movieTitle + ", favorite = " + movieData.favorite);
//            toastMessage = "Added to favorites";
//
//        } else if (MovieFragment.currentMovie.favorite) {
//            favoriteButton.setImageResource(R.drawable.outline_grade_black_36dp);
//            MovieFragment.currentMovie.favorite = false;
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
