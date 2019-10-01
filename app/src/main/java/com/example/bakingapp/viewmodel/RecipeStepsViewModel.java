package com.example.bakingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.bakingapp.model.StepsItem;

import java.util.List;

public class RecipeStepsViewModel extends AndroidViewModel {
    List<StepsItem> stepsItemList;

    public RecipeStepsViewModel(@NonNull Application application) {
        super(application);
    }

    public List<StepsItem> getStepsItemList() {
        return stepsItemList;
    }

    public void setStepsItemList(List<StepsItem> stepsItemList) {
        this.stepsItemList = stepsItemList;
    }

    public StepsItem getNextItem(int id) {

        for (int i = 0; i < stepsItemList.size() - 1; i++)
            if (id == stepsItemList.get(i).getId())
                return stepsItemList.get(i + 1);
        return null;

    }

    public StepsItem getStep(int stepId) {
        for (StepsItem stepsItem : stepsItemList)
            if (stepId == stepsItem.getId())
                return stepsItem;
        return null;
    }

    public StepsItem getPreviousItem(int id) {
        for (int i = 1; i < stepsItemList.size() ; i++)
            if (id == stepsItemList.get(i).getId())
                return stepsItemList.get(i - 1);
        return null;
    }
}
