package org.udacity.android.movieproject1;

import android.os.AsyncTask;
import android.util.Log;

public class MovieAsyncTask extends AsyncTask<String, Void, String> {

    //Removed logic to obtain API key from XML file, hardcoded the API key in NetworkUtil

    private static final String TAG = MovieAsyncTask.class.getSimpleName();
    private OnTaskCompleted taskCompleted;
    private String query;
    private int movieID;

    MovieAsyncTask(OnTaskCompleted activityContext, String query) {
        this.query = query;
        this.taskCompleted = activityContext;
    }

    MovieAsyncTask(OnTaskCompleted activityContext, String query, int movieID) {
        this.query = query;
        this.taskCompleted = activityContext;
        this.movieID = movieID;
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonString;
        if (query.equals("trailer") || query.equals("reviews"))
            jsonString = NetworkUtil.sendQueryURL(query, movieID);
        else
            jsonString = NetworkUtil.sendQueryURL(query);
        return jsonString;
    }


    @Override
    protected void onPostExecute(String jsonString) {
        Log.i(TAG, "from onPostExecute: " + jsonString);
        taskCompleted.onTaskCompleted(jsonString);
    }
}

