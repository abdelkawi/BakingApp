package com.example.bakingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bakingapp.model.RecipeItem;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Query("SELECT * FROM recipe ORDER BY id")
    LiveData<List<RecipeItem>> loadRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeItem movieEntry);
    @Query("SELECT * FROM recipe WHERE id=:id")
    RecipeItem getRecipe(int id);
}
