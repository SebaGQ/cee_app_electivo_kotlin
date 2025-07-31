package com.kotlin.cee_app.data

import androidx.room.TypeConverter
import java.time.LocalDate
import com.kotlin.cee_app.data.entity.EstadoVotacion

class Converters {
    @TypeConverter
    fun fromEpochDay(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun toEpochDay(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun fromEstado(value: EstadoVotacion?): String? = value?.label

    @TypeConverter
    fun toEstado(value: String?): EstadoVotacion? =
        value?.let { EstadoVotacion.fromString(it) }
}
