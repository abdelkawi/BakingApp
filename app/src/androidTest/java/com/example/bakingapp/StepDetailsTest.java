package com.example.bakingapp;

import android.content.Intent;


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.bakingapp.R;
import com.example.bakingapp.RecipeDetailActivity;

import org.junit.Before;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;

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


