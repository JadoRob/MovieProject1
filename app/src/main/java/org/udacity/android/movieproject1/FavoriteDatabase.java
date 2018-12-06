package org.udacity.android.movieproject1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieData.class}, version = 2, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();
    public static final String LOG_TAG = FavoriteDatabase.class.getSimpleName();
    public static final String DATABASE_NAME = "favorite_movies";
    private static volatile FavoriteDatabase INSTANCE;

    public static FavoriteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            //this only occurs if there is no database, (singleton class)
            synchronized (FavoriteDatabase.class) {
                if (INSTANCE == null) {
                    Log.d(LOG_TAG, "Generating Favorites Database");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteDatabase.class, FavoriteDatabase.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        //if database already exists, the instance is returned.
        Log.d(LOG_TAG, "Retrieving instance of favorites database");
        return INSTANCE;
    }

}
