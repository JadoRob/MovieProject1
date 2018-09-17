package org.udacity.android.movieproject1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetMoviesTask extends AsyncTask<String, Void, String> {

    private static final String TAG = GetMoviesTask.class.getSimpleName();
    private OnTaskCompleted taskCompleted;
    private Context context;
    String queryString;

    public GetMoviesTask(OnTaskCompleted activityContext, Context context, String queryString) {
        this.context = context;
        this.taskCompleted = activityContext;
        this.queryString = queryString;
    }

    @Override
    protected String doInBackground(String... strings) {

        String jsonString;
        String apiKey = context.getString(R.string.tmdb_api_key);
        if (queryString == "movies"){
            queryString = MoviePreferences.getMovieSortOrder(context);
        }
        jsonString = NetworkUtil.getMovieInfo(queryString, apiKey);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String jsonString) {
        Log.i(TAG, "from onPostExecute: " + jsonString);
        taskCompleted.onTaskCompleted(jsonString);
    }
}
