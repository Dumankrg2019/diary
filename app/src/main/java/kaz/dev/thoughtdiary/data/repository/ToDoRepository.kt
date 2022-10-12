package kaz.dev.thoughtdiary.data.repository

import androidx.lifecycle.LiveData
import kaz.dev.thoughtdiary.data.ToDoDao
import kaz.dev.thoughtdiary.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun  insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }
}