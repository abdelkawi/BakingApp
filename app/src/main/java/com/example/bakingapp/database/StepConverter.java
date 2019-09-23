package com.example.bakingapp.database;


import androidx.room.TypeConverter;

import com.example.bakingapp.model.StepsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class StepConverter {

    @TypeConverter
    public static List<StepsItem> storedStringToMyObjects(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<StepsItem>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String myObjectsToStoredString(List<StepsItem> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }

}
