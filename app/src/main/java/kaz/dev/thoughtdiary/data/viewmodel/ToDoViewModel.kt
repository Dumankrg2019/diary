package kaz.dev.thoughtdiary.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kaz.dev.thoughtdiary.data.TodoDatabase
import kaz.dev.thoughtdiary.data.models.ToDoData
import kaz.dev.thoughtdiary.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application): AndroidViewModel(application) {

    private val toDoDao = TodoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository

    val getAllData: LiveData<List<ToDoData>>
    val sortByHighPriority:LiveData<List<ToDoData>>
    val sortByLowPriority:LiveData<List<ToDoData>>

    init {
        repository = ToDoRepository(toDoDao)
        getAllData = repository.getAllData
        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateDate(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery:String): LiveData<List<ToDoData>> {
        return repository.searchDatabase(searchQuery)
    }
}