package com.kotlin.cee_app.ui

import androidx.test.core.app.ApplicationProvider
import com.kotlin.cee_app.ui.elections.viewmodel.CreateElectionViewModel
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreateElectionViewModelTest {
    @Test
    fun validate_date_order() {
        val vm = CreateElectionViewModel(ApplicationProvider.getApplicationContext())
        vm.setFechaInicio(LocalDate.of(2024,1,1))
        vm.setFechaFin(LocalDate.of(2023,1,1))
        var ok = true
        vm.guardar("t", "d", { ok = false }, { ok = true })
        assertFalse(ok)
        vm.setFechaFin(LocalDate.of(2024,1,2))
        vm.guardar("t", "d", { ok = false }, { ok = true })
        assertTrue(ok)
    }

    @Test
    fun require_two_options() {
        val vm = CreateElectionViewModel(ApplicationProvider.getApplicationContext())
        vm.agregarOpcion("A")
        var ok = true
        vm.guardar("t","d",{ ok = false }, { ok = true })
        assertFalse(ok)
        vm.agregarOpcion("B")
        vm.guardar("t","d",{ ok = false }, { ok = true })
        assertTrue(ok)
    }
}
