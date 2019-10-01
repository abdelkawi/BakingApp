package com.example.bakingapp;


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.bakingapp.ui.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4ClassRunner.class)
public class StepDetailsTest {

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mRecipeDetailActivity = new ActivityTestRule<>(RecipeDetailActivity.class);

    @Test
    public void testStepDetail() {


        onView(withId(R.id.tv_recipe_step_detail)).check(matches(isDisplayed()));
    }

}


