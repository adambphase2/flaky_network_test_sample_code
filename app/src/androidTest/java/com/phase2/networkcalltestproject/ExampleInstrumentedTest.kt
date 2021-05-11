package com.phase2.networkcalltestproject

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.AssertionFailedError
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test(expected=androidx.test.espresso.NoMatchingViewException::class)
    fun flakeyTest() {
        onView(withId(R.id.button_first)).perform(click())
        onView(withId(R.id.button_second)).perform(click())
        onView(withId(R.id.button_first)).check(matches(isDisplayed()))
    }

    @Test
    //Sleep
    fun badTest() {
        onView(withId(R.id.button_first)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.button_second)).perform(click())
        onView(withId(R.id.button_first)).check(matches(isDisplayed()))
    }


    @Test
    fun idleResourcesTest() {
        rule.scenario.onActivity {
            activity ->

            RxJavaPlugins.setInitComputationSchedulerHandler(
                Rx2Idler.create("RxJava 2.x Computation Scheduler"));
            RxJavaPlugins.setInitIoSchedulerHandler(
                Rx2Idler.create("RxJava 2.x IO Scheduler"));


            val ioScheduler = Rx2Idler.wrap(activity.schedulers.iOScheduler, "io Scheduler")
            IdlingRegistry.getInstance().register(ioScheduler)
            val mainThreadScheduler = Rx2Idler.wrap(activity.schedulers.mainThreadScheduler, "main Thread Scheduler")
            IdlingRegistry.getInstance().register(mainThreadScheduler)

            activity.schedulers.iOScheduler = ioScheduler
            activity.schedulers.mainThreadScheduler = mainThreadScheduler
        }


        onView(withId(R.id.button_first)).perform(click())
        onView(withId(R.id.button_second)).perform(click())
        onView(withId(R.id.button_first)).check(matches(isDisplayed()))
    }

    @Test
    fun idleViewMatcherTest() {
        onView(withId(R.id.button_first)).perform(click())
        onViewEnabled(withId(R.id.button_second)).perform(click())
        onView(withId(R.id.button_first)).check(matches(isDisplayed()))
    }

    fun onViewEnabled(viewMatcher: Matcher<View>): ViewInteraction {
        val isEnabled: ()->Boolean = {
            var isDisplayed = false
            try {
                onView(viewMatcher).check(matches((ViewMatchers.isEnabled())))
                isDisplayed = true
            }
            catch (e: AssertionFailedError) { isDisplayed = false }
            isDisplayed
        }
        for (x in 0..9) {
            Thread.sleep(400)
            if (isEnabled()) {
                break
            }
        }
        return Espresso.onView(viewMatcher)
    }
}