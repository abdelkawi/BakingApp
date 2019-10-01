package com.example.bakingapp;



import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;

import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.bakingapp.ui.RecipeListActivity;


import org.hamcrest.Matchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeClickTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void recipeClickTest2() throws Throwable {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.rv_recipes_display))
                .inRoot(RootMatchers.withDecorView(Matchers.is(mActivityTestRule.getActivity().getWindow().getDecorView()))).
                perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }

}
