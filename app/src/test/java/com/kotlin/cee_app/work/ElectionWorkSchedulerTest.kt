package com.kotlin.cee_app.work

import org.junit.Assert.assertTrue
import org.junit.Test

class ElectionWorkSchedulerTest {
    @Test
    fun workInterval_is_at_least_fifteen_minutes() {
        assertTrue(ElectionWorkScheduler.WORK_INTERVAL_MINUTES >= 15L)
    }
}
