package org.udacity.android.movieproject1;

import android.content.Context;
import android.preference.PreferenceManager;

public class MoviePreferences {

    private static final String MOVIE_SORT_ORDER = "sortOrder";

    public static String getMovieSortOrder(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(MOVIE_SORT_ORDER, "popular");
    }

    public static void setMovieSortOrder(Context context, String movieOrder) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(MOVIE_SORT_ORDER, movieOrder)
                .apply();
    }

}
