package com.example.bakingapp.ui.activity;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.bakingapp.R;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsTest {
    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mRecipeDetailActivity = new ActivityTestRule<>(RecipeDetailsActivity.class);
    @Test
    public void reipeDetailTest(){
        onView(withId(R.id.tv_Ingredient)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipe_step_list))
                .inRoot(RootMatchers.withDecorView(Matchers.is(mRecipeDetailActivity.getActivity().getWindow().getDecorView()))).
                perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

    }
}
