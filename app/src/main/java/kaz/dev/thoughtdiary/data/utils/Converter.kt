package kaz.dev.thoughtdiary.data.utils

import androidx.room.TypeConverter
import kaz.dev.thoughtdiary.data.models.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority):String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}