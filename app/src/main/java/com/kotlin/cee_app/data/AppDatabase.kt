package com.kotlin.cee_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Entities
import com.kotlin.cee_app.data.AdminEntity
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.SimpleEntity
import com.kotlin.cee_app.data.UsuarioEntity
import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.data.VotoEntity

@Database(
    entities = [
        UsuarioEntity::class,
        AdminEntity::class,
        SimpleEntity::class,
        VotacionEntity::class,
        OpcionEntity::class,
        VotoEntity::class,
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun adminDao(): AdminDao
    abstract fun simpleDao(): SimpleDao
    abstract fun votacionDao(): VotacionDao
    abstract fun opcionDao(): OpcionDao
    abstract fun votoDao(): VotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
