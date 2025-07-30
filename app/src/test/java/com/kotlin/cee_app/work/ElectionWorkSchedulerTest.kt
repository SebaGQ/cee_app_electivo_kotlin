package com.kotlin.cee_app.work

import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ElectionWorkSchedulerTest {
    @Test
    fun nextDelayMinutes_returns_one_minute_delay() {
        val now = LocalDateTime.of(2024, 1, 1, 10, 30)
        val minutes = ElectionWorkScheduler.nextDelayMinutes(now)
        assertEquals(1L, minutes)
    }
}
