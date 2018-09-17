package org.udacity.android.movieproject1;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR_MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    private static final String TOP_RATED_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";
    private static final String TRAILERS_ENDPOINT = "/videos?";
    private static final String REVIEWS_ENDPOINT = "/reviews?";
    private static final String LANGUAGE = "language";
    private static final String PAGE = "page";
    private static final String API_KEY = "api_key";

    static String getMovieInfo(String queryString, String apiKey) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieBaseURL = null;
        String movieJSONString = null;


        //initialize base http with "popular" query
        if (queryString.equals("popular")) {
            movieBaseURL = POPULAR_MOVIE_BASE_URL;
        } else if (queryString.equals("rating")) {
            movieBaseURL = TOP_RATED_BASE_URL;
        } else if (queryString.equals("trailers")){
            movieBaseURL = BASE_URL + MovieFragment.currentMovie.movieID + TRAILERS_ENDPOINT;
        } else if (queryString.equals("reviews")) {
            movieBaseURL = BASE_URL + MovieFragment.currentMovie.movieID + REVIEWS_ENDPOINT;
        }

        //referenced example code from https://google-developer-training.gitbooks.io/android-developer-fundamentals-course-practicals/content/en/Unit%203/72_p_asynctask_asynctaskloader.html

        try {

            Uri buildURI = Uri.parse(movieBaseURL).buildUpon()
                    .appendQueryParameter(API_KEY, apiKey)
                    .appendQueryParameter(LANGUAGE, "en-US")
                    .appendQueryParameter(PAGE, "1").build();
            URL requestURL = new URL(buildURI.toString());
            Log.i(TAG, requestURL.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            //Throws an exception if this network operation is performed on the main thread!
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            movieJSONString = buffer.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, "Unable to collect movie data");
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return movieJSONString;
        }

    }

    private static void loadFromDB() {

    }
}
