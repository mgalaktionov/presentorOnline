package com.example.mikhail.mobilepresentor;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntroActivityTest {

    @Rule
    public ActivityTestRule<IntroActivity> mActivityTestRule = new ActivityTestRule<>(IntroActivity.class);

    @Test
    public void introActivityTest() {
        ViewInteraction imageView = onView(
                allOf(withId(R.id.slideimg),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.txttitle), withText("STEP 1"),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("STEP 1")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.txtdescription), withText("visit presentor.online"),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("visit presentor.online")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.txtskip), withText("swipe right"),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                3),
                        isDisplayed()));
        textView3.check(matches(withText("swipe right")));

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.viewpager),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.slideimg),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                0),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.txttitle), withText("STEP 2"),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("STEP 2")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.txtskip), withText("swipe right"),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                3),
                        isDisplayed()));
        textView5.check(matches(withText("swipe right")));

        ViewInteraction viewPager2 = onView(
                allOf(withId(R.id.viewpager),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        viewPager2.perform(swipeLeft());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.txtskip), withText("tap to start"),
                        childAtPosition(
                                allOf(withId(R.id.slidelinearlayout),
                                        withParent(withId(R.id.viewpager))),
                                3),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
