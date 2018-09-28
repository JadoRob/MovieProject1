package org.udacity.android.movieproject1;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

    private final static String TAG = NetworkUtil.class.getSimpleName();
    private final static String BASE_URL = "https://api.themoviedb.org/3/";
    private final static String POPULAR_URL_ENDPOINT = "movie/popular";
    private final static String TOP_RATED_URL_ENDPOINT = "movie/top_rated";
    private final static String LANGUAGE = "language";
    private final static String PAGE = "page";
    private final static String API_KEY = "api_key";

    private static HttpURLConnection urlConnection = null;
    private static BufferedReader reader = null;
    private static String movieJSONString = null;


    static String sendQueryURL(String query) {
        String queryURL = null;
        String movieID = null;

        switch (query) {
            case "popular":
                queryURL = BASE_URL + POPULAR_URL_ENDPOINT;
                break;
            case "rating":
                queryURL = BASE_URL + TOP_RATED_URL_ENDPOINT;
                break;
            case "trailer":
                queryURL = BASE_URL + "movie/" + movieID + "/videos";
                break;
            case "reviews":
                queryURL = BASE_URL + "movie/" + movieID + "/videos";

        }
        return  getJsonString(queryURL);
    }

    @Nullable
    private static String getJsonString(String queryURL) {
        String jsonString;
        Log.i(TAG, queryURL);
        try {

            // FOR TESTING PURPOSES, REMOVE PRIOR TO PUBLISHING!
            String apiKey = "API KEY GOES HERE!";

            Uri buildURI = Uri.parse(queryURL).buildUpon()
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
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            jsonString = buffer.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, "Unable to collect movie data: " + queryURL);
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
        }
        return jsonString;
    }

}
