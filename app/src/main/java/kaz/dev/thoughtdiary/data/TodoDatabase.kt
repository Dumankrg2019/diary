package kaz.dev.thoughtdiary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kaz.dev.thoughtdiary.data.utils.Converter
import kaz.dev.thoughtdiary.data.models.ToDoData

@Database(entities = arrayOf(ToDoData::class), version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun toDoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}