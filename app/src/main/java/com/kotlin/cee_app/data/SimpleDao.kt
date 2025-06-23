package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SimpleDao {
    @Insert
    suspend fun insert(simple: SimpleEntity)

    @Query("SELECT * FROM simples")
    fun getAll(): Flow<List<SimpleEntity>>
}
