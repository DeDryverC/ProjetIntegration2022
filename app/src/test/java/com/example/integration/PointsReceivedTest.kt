package com.example.integration

import androidx.test.core.app.ApplicationProvider
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PointsReceivedTest {
    private lateinit var scenario: ActivityScenario<PointsReceivedActivity>

    @Before
    fun setUp() {
        scenario = launchActivity()
    }

    @Test
    fun toCreate(){
        scenario.moveToState(Lifecycle.State.CREATED);
    }



}