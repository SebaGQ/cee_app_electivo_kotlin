package com.kotlin.cee_app.ui

import androidx.test.core.app.ApplicationProvider
import com.kotlin.cee_app.ui.users.viewmodel.CreateUserViewModel
import org.junit.Test
import org.junit.Assert.*

class CreateUserViewModelTest {
    @Test
    fun require_all_fields_and_valid_email() {
        val vm = CreateUserViewModel(ApplicationProvider.getApplicationContext())
        var ok = true
        vm.guardar("", "mail@test.com", "1234", "SIMPLE", { ok = false }, { ok = true })
        assertFalse(ok)
        vm.guardar("Name", "bademail", "1234", "SIMPLE", { ok = false }, { ok = true })
        assertFalse(ok)
        vm.guardar("Name", "good@mail.com", "1234", "SIMPLE", { ok = false }, { ok = true })
        assertTrue(ok)
    }
}
