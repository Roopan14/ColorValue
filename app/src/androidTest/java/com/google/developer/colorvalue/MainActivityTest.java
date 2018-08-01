package com.google.developer.colorvalue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Roopan C on 7/21/2018.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = activityTestRule.getActivity();
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
        mainActivity = null;
    }

    @Test
    public void fabClick()
    {

        assertNotNull(mainActivity.findViewById(R.id.fab));
        onView(withId(R.id.fab)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), AddCardActivity.class)));

    }
}