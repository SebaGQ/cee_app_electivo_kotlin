package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {
    @Insert
    suspend fun insert(admin: AdminEntity)

    @Query("SELECT * FROM admins")
    fun getAll(): Flow<List<AdminEntity>>
}
