package org.udacity.android.movieproject1;

import android.content.res.Resources;
import android.os.AsyncTask;

public class getMovieData extends AsyncTask<String, Void, String> {


    //sortType "popular" string is passed from MainActivity when this class is called

    @Override
    protected String doInBackground(String... params) {
        return NetworkUtil.getMovieInfo(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
