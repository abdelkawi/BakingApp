package com.example.bakingapp;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.bakingapp.ui.StepListActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4ClassRunner.class)
public class RecipeDetailsTest {
    @Rule
    public ActivityTestRule<StepListActivity> mRecipeDetailActivity = new ActivityTestRule<>(StepListActivity.class);
    @Test
    public void reipeDetailTest(){
        onView(withId(R.id.tv_Ingredient)).check(matches(isDisplayed()));
        onView(withId(R.id.item_list))
                .inRoot(RootMatchers.withDecorView(Matchers.is(mRecipeDetailActivity.getActivity().getWindow().getDecorView()))).
                perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

    }
}
