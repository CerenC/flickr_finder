package com.cerenb.flickrfinder.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

/**
 * Creates a new [CoroutineScope] with the rule's testDispatcher
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun MainCoroutineRule.coroutineScope(): CoroutineScope = TestScope(testDispatcher)