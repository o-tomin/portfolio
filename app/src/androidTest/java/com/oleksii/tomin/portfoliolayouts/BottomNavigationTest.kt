package com.oleksii.tomin.portfoliolayouts

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oleksii.tomin.portfoliolayouts.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomNavigationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testProfileFragmentIsDisplayedOnClick() {
        onView(withId(R.id.bottom_nav_profile)).perform(click())
        Thread.sleep(1_000)
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun testExperienceFragmentIsDisplayedOnClick() {
        onView(withId(R.id.bottom_nav_experience)).perform(click())
        Thread.sleep(1_000)
        onView(withId(R.id.experience_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun testEducationFragmentIsDisplayedOnClick() {
        onView(withId(R.id.bottom_nav_education)).perform(click())
        Thread.sleep(1_000)
        onView(withId(R.id.education_fragment)).check(matches(isDisplayed()))
    }
}