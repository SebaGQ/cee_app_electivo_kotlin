package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.ConteoOpcion
import com.kotlin.cee_app.ui.elections.viewmodel.toPercent
import org.junit.Assert.assertEquals
import org.junit.Test

class OptionUtilsTest {
    @Test
    fun percent_conversion_valid() {
        val list = listOf(
            ConteoOpcion("A", 2),
            ConteoOpcion("B", 1)
        )
        val result = toPercent(list)
        assertEquals(2, result.size)
        assertEquals(66, result[0].porcentaje)
        assertEquals(33, result[1].porcentaje)
    }
}
