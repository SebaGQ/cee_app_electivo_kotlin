package com.kotlin.cee_app.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(private val db: AppDatabase) {

    val usuarios: Flow<List<UsuarioEntity>> = db.usuarioDao().getAll()

    suspend fun insertar(usuario: UsuarioEntity) =
        db.usuarioDao().insert(usuario)

    suspend fun actualizar(usuario: UsuarioEntity) =
        db.usuarioDao().update(usuario)

    suspend fun eliminar(id: String) =
        db.usuarioDao().deleteById(id)

    suspend fun obtener(id: String): UsuarioEntity? =
        db.usuarioDao().findById(id)

    companion object {
        @Volatile private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = UserRepository(AppDatabase.getDatabase(context))
                INSTANCE = repo
                repo
            }
        }
    }
}
