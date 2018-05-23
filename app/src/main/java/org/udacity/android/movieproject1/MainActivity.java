package org.udacity.android.movieproject1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //used as a "query" to show sort order "popular", and is passed as a string to AsyncTask class
    //"getMovieData".
    String sortType = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //- sortType - parameter shows an error "cannot be applied to java.lang.string" app is able to display
        //the dummy data in a gridview using dummy data from MovieFragment.

        //new getMovieData(sortType).execute();

    }



}
