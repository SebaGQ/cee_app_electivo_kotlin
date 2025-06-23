package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VotoDao {
    @Insert
    suspend fun insert(voto: VotoEntity): Long

    @Query("SELECT * FROM votos")
    fun getAll(): Flow<List<VotoEntity>>
}
