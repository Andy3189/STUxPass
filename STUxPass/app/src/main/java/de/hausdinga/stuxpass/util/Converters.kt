package de.hausdinga.stuxpass.util

import androidx.room.TypeConverter
import java.lang.Exception
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun dateFromISOString(value: String?): LocalDateTime? {
        return value?.let {
            try {
                LocalDateTime.parse(it)
            } catch (ex: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun dateToISOString(date: LocalDateTime?): String? {
        return date?.toString()
    }
    @TypeConverter
    fun fromListStringToString(list: List<String>): String = list.joinToString()
    @TypeConverter
    fun toListStringFromString(data: String): List<String> = data.split(",")
}
