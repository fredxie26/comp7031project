package com.example.comp7031project;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    private String firstName = "New";
    private String lastName = "User";
    private String address = "123 Test Address";

    @Before
    public void setUp() {
    }

    @Test
    public void testUserFlow() {
        onView(withId(R.id.button_create_profile))
                .perform(click());

        onView(withId(R.id.edit_first_name))
                .perform(typeText("New"));
        onView(withId(R.id.edit_last_name))
                .perform(typeText("User"));
        onView(withId(R.id.edit_address))
                .perform(typeText("123 Main St"));

        onView(withId(R.id.button_save))
                .perform(click());

        onView(withId(R.id.searchEditText))
                .perform(typeText("New"));
        onView(withId(R.id.searchButton))
                .perform(click());

        onView(withText("Name: New User"))
                .perform(click());

        onView(withId(R.id.button_back))
                .perform(click());

        onView(withText("Name: New User"))
                .check(matches(isDisplayed()));

    }
}
