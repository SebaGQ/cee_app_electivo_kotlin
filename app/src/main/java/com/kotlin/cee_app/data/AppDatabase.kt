package com.kotlin.cee_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Entities
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.data.entity.UsuarioEntity
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.VotoEntity

// Daos
import com.kotlin.cee_app.data.dao.UsuarioDao
import com.kotlin.cee_app.data.dao.VotacionDao
import com.kotlin.cee_app.data.dao.OpcionDao
import com.kotlin.cee_app.data.dao.VotoDao

@Database(
    entities = [
        UsuarioEntity::class,
        VotacionEntity::class,
        OpcionEntity::class,
        VotoEntity::class,
    ],
    version = 6,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
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
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
