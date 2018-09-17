package org.udacity.android.movieproject1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieData.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite_movies";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            //this only occurs if there is no database, (singleton class)
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Generating Favorites Database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        //.fallbackToDestructiveMigration()
                        .allowMainThreadQueries() //TEMPORARILY USED TO CONFIRM FUNCTIONALITY
                        .build();
            }
        }
        //if database already exists, the instance is returned.
        Log.d(LOG_TAG, "Retrieving instance of favorites database");
        return sInstance;
    }

    public abstract MovieDao movieDao();

}
