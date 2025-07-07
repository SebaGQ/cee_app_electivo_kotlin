package com.kotlin.cee_app.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.kotlin.cee_app.data.AppDatabase
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.data.VotoEntity
import com.kotlin.cee_app.ui.elections.viewmodel.ElectionsViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class ElectionsViewModelHasVotedTest {
    private lateinit var repo: ElectionRepository
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repo = ElectionRepository.getInstance(context)
        db = AppDatabase.getDatabase(context)
        db.clearAllTables()
    }

    @Test
    fun hasVoted_set_contains_correct_ids() = runBlocking {
        val v1 = VotacionEntity("v1", "t", "d", LocalDate.now(), LocalDate.now(), "Abierta", "a")
        val v2 = VotacionEntity("v2", "t", "d", LocalDate.now(), LocalDate.now(), "Abierta", "a")
        db.votacionDao().insert(v1)
        db.votacionDao().insert(v2)
        val op1 = db.opcionDao().insert(OpcionEntity("A", "v1"))
        db.opcionDao().insert(OpcionEntity("B", "v2"))
        db.votoDao().insert(VotoEntity(LocalDate.now(), op1, "admin1", "v1"))

        val vm = ElectionsViewModel(ApplicationProvider.getApplicationContext())
        vm.refresh()
        assertTrue(vm.hasVoted.value.contains("v1"))
        assertFalse(vm.hasVoted.value.contains("v2"))
    }
}
