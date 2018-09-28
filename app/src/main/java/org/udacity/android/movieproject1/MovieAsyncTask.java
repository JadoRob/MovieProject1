package org.udacity.android.movieproject1;

import android.os.AsyncTask;
import android.util.Log;

public class MovieAsyncTask extends AsyncTask<String, Void, String> {

    //Removed logic to obtain API key from XML file, hardcoded the API key in NetworkUtil

    private static final String TAG = MovieAsyncTask.class.getSimpleName();
    private OnTaskCompleted taskCompleted;
    private String query;

    MovieAsyncTask(OnTaskCompleted activityContext, String query) {
        this.query = query;
        this.taskCompleted = activityContext;
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonString;
        jsonString = NetworkUtil.sendQueryURL(query);
        return jsonString;
    }


    @Override
    protected void onPostExecute(String jsonString) {
        Log.i(TAG, "from onPostExecute: " + jsonString);
        taskCompleted.onTaskCompleted(jsonString);
    }
}
