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
        // Prepare initial state if needed
    }

    @Test
    public void testUserFlow() {
        onView(withId(R.id.button_create_profile))
                .perform(click());

        // Step 2: Input first name, last name, and address, then save
        onView(withId(R.id.edit_first_name))
                .perform(typeText("New"));
        onView(withId(R.id.edit_last_name))
                .perform(typeText("User"));
        onView(withId(R.id.edit_address))
                .perform(typeText("123 Main St"));

        // Simulate taking a photo (this can be done by using an image from resources or emulator)
//        onView(withId(R.id.button_take_photo))
//                .perform(click());  // Add photo action here if needed

        onView(withId(R.id.button_save))
                .perform(click());  // Save profile

        // Step 3: Test Focus functionality
//        onView(withId(R.id.image_star))  // Focus star for the new user
//                .perform(click());
//
//        // Step 4: Test filter focused functionality
//        onView(withId(R.id.button_filter))
//                .perform(click());

        // Step 5: Test filtering on "New User"
        onView(withId(R.id.searchEditText))
                .perform(typeText("New"));
        onView(withId(R.id.searchButton))  // Click filter button again
                .perform(click());

        // Step 6: Click New User to go to the detail page
        onView(withText("Name: New User"))
                .perform(click());  // Navigate to the detail view

        // Step 7: Go back to the main page from the detail page
        onView(withId(R.id.button_back))  // Assuming this is the back button
                .perform(click());

        onView(withText("Name: New User"))
                .check(matches(isDisplayed()));

// Step 2: Find the parent RelativeLayout of the "New User" and locate the Delete button within it
//        onView(allOf(withId(R.id.button_delete),
//                hasSibling(withText("Name: New User"))))  // Use the sibling TextView to locate Delete button
//                .perform(click());
//
//        // Step 4: Verify that the "New User" is deleted (check that it's no longer in the list)
//        onView(withText("Name: New User"))
//                .check(doesNotExist());

    }
}
