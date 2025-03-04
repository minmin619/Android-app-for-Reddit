package edu.cs371m.reddit

import androidx.test.rule.ActivityTestRule


import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import edu.cs371m.reddit.RecyclerViewChildActions.Companion.actionOnChild
import edu.cs371m.reddit.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import edu.cs371m.reddit.ui.PostRowAdapter
import org.junit.Assert.assertTrue
import org.hamcrest.CoreMatchers.equalTo


/**
 * [Testing Fundamentals](http://d.android.com/tools/testing/testing_android.html)
 */
// the setup for this test is based on TestFetch that's available on the original project
// I need something like fetch complete to populate the list
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class InstrumentedApplicationTest {

    @Rule @JvmField
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    // Warning, these don't work.
    @Test
    fun testFirstElement() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.title,
                    0,
                    withText("Osaka Aquarium just stepped up their gift shop game with these fat seal plushies"))
                )
            )
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.score,
                    0,
                    withText("111372")
            )
        ))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.comments,
                    0,
                    withText("1226")
                )
            )
        )
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.image,
                    0,
                    withTagValue(equalTo("bigcat0.jpg"))
                )
            )
        )
    }
}