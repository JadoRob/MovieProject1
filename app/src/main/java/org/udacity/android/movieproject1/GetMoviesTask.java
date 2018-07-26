package org.udacity.android.movieproject1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetMoviesTask extends AsyncTask<String, Void, String> {

    private static final String TAG = GetMoviesTask.class.getSimpleName();
    private OnTaskCompleted taskCompleted;
    private Context context;

    public GetMoviesTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        String jsonString;
        String apiKey = context.getString(R.string.tmdb_api_key);
        String queryString = MoviePreferences.getMovieSortOrder(context);
        jsonString = NetworkUtil.getMovieInfo(queryString, apiKey);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String jsonString) {
        Log.i(TAG, "from onPostExecute: " + jsonString);
        taskCompleted.onTaskCompleted(jsonString);
    }
}