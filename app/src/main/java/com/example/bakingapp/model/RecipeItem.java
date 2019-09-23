package com.example.bakingapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.bakingapp.database.IngredientsConverter;
import com.example.bakingapp.database.StepConverter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "recipe")
public class RecipeItem {

    @SerializedName("image")
    private String image;

    @SerializedName("servings")
    private int servings;

    @SerializedName("name")
    private String name;

    @TypeConverters(IngredientsConverter.class)
    @SerializedName("ingredients")
    private List<IngredientsItem> ingredients;

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @TypeConverters(StepConverter.class)
    @SerializedName("steps")
    private List<StepsItem> steps;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getServings() {
        return servings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIngredients(List<IngredientsItem> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientsItem> getIngredients() {
        return ingredients;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSteps(List<StepsItem> steps) {
        this.steps = steps;
    }

    public List<StepsItem> getSteps() {
        return steps;
    }

    public RecipeItem(String image, int servings, String name, List<IngredientsItem> ingredients, int id, List<StepsItem> steps) {
        this.image = image;
        this.servings = servings;
        this.name = name;
        this.ingredients = ingredients;
        this.id = id;
        this.steps = steps;
    }

    @Override
    public String toString() {
        return
                "RecipeItem{" +
                        "image = '" + image + '\'' +
                        ",servings = '" + servings + '\'' +
                        ",name = '" + name + '\'' +
                        ",ingredients = '" + ingredients + '\'' +
                        ",id = '" + id + '\'' +
                        ",steps = '" + steps + '\'' +
                        "}";
    }
}