package com.example.bakingapp.ui.activity;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.bakingapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class StepDetailsTest {

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mRecipeDetailActivity = new ActivityTestRule<>(RecipeDetailsActivity.class);

    @Test
    public void testStepDetail() {


        onView(withId(R.id.recipestep_detail)).check(matches(isDisplayed()));
    }

}


