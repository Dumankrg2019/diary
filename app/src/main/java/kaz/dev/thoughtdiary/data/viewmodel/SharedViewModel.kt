package kaz.dev.thoughtdiary.data.viewmodel

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kaz.dev.thoughtdiary.R
import kaz.dev.thoughtdiary.data.TodoDatabase
import kaz.dev.thoughtdiary.data.models.Priority
import kaz.dev.thoughtdiary.data.models.ToDoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(todoData: List<ToDoData>) {
        emptyDatabase.value = todoData.isEmpty()
    }

    val listener : AdapterView.OnItemSelectedListener = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position) {
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.blue))}
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}
            }
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

     fun verifyDataFromUser(title:String, description:String):Boolean {
         return !(title.isEmpty() || description.isEmpty())
    }

     fun parsePriority(priority:String): Priority {
        return when(priority) {
            "High Priority" -> {
                Priority.HIGH}
            "Medium Priority" -> {
                Priority.MEDIUM}
            "Low Priority" -> {
                Priority.LOW}
            else -> {
                Priority.LOW}
        }
    }

     fun parsePriorityToInt(priority: Priority): Int {
        return when(priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }
}