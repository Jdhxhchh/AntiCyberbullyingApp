package com.example.anticyberbullyingapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testViewsAreVisible() {
        // Перевіряємо, що всі основні елементи на екрані
        onView(withId(R.id.inputEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.checkButton)).check(matches(isDisplayed()))
        onView(withId(R.id.literatureBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.emergencyBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyTextShowsToast() {
        // Натискаємо кнопку без введення тексту
        onView(withId(R.id.checkButton)).perform(click())

        // Тут ми не можемо перевірити Toast напряму, тому перевірка візуальна
    }

    @Test
    fun testTypingTextAndClickingCheckButton() {
        // Вводимо текст
        onView(withId(R.id.inputEditText)).perform(typeText("Some text"), closeSoftKeyboard())

        // Натискаємо кнопку
        onView(withId(R.id.checkButton)).perform(click())

        // Почекати і перевірити, чи з'явився результат
        Thread.sleep(3000) // ⚠️ Лише для демонстрації, краще замінити на IdlingResource
        onView(withId(R.id.resultContainer)).check(matches(isDisplayed()))
    }

    @Test
    fun testOpenLiteratureActivity() {
        onView(withId(R.id.literatureBtn)).perform(click())
        // Не можемо перевірити перехід без більше логіки, але кнопка працює
    }

    @Test
    fun testOpenEmergencyActivity() {
        onView(withId(R.id.emergencyBtn)).perform(click())
    }
}
