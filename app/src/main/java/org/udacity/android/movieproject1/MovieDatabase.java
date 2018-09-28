package org.udacity.android.movieproject1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieData.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();
    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "favorite_movies";
    private static volatile MovieDatabase INSTANCE;

    public static MovieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            //this only occurs if there is no database, (singleton class)
            synchronized (MovieDatabase.class) {
                Log.d(LOG_TAG, "Generating Favorites Database");
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries() //TEMPORARY, USED TO CONFIRM FUNCTIONALITY
                            .build();
                }

            }
        }
        //if database already exists, the instance is returned.
        Log.d(LOG_TAG, "Retrieving instance of favorites database");
        return INSTANCE;
    }



}
