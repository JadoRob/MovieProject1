package org.udacity.android.movieproject1;

import android.content.res.Resources;
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
    private static final String POPULAR_MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    private static final String TOP_RATED_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";
    private static final String LANGUAGE = "language";
    private static final String PAGE = "page";
    //created a string resource file for the key, and added it gitignore
    private static final String API_KEY = Resources.getSystem().getString(R.string.key);


    static String getMovieInfo(String queryString) {

        //the parameter queryString contains "popular" to determine the http query

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieBaseURL = null;
        String movieJSONString = null;

        //initialize base http with "popular" query
        if (queryString.equals("popular")) {
            movieBaseURL = POPULAR_MOVIE_BASE_URL;

        } else if (queryString.equals("top rated")) {
            movieBaseURL = TOP_RATED_BASE_URL;
        }

        //referenced example code from https://google-developer-training.gitbooks.io/android-developer-fundamentals-course-practicals/content/en/Unit%203/72_p_asynctask_asynctaskloader.html

        try {
            Uri buildURI = Uri.parse(movieBaseURL).buildUpon()
                    .appendQueryParameter(API_KEY, "api_key")
                    .appendQueryParameter(LANGUAGE, "en-US")
                    .appendQueryParameter(PAGE, "1").build();
            Log.v(TAG, buildURI.toString());
            URL requestURL = new URL(buildURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
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
            Log.d(TAG, movieJSONString);
            return movieJSONString;
        }

    }
}
