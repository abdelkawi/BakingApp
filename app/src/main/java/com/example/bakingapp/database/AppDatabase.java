package com.example.bakingapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bakingapp.model.RecipeItem;

@TypeConverters(IngredientsConverter.class)
@Database(entities = {RecipeItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    private static final String DATABASE_NAME = "recipes";
    private static Object LOCK = new Object();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext()
                        , AppDatabase.class, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }
    public abstract RecipeDAO taskDAO();
}
