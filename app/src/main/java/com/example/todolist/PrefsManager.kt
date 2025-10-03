package com.example.todolist

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PrefsManager {
    private const val PREF_NAME = "todo_prefs"
    private const val TASKS_KEY = "tasks"

    fun saveTasks(context: Context, tasks: List<Pair<String, String>>) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(tasks)
        editor.putString(TASKS_KEY, json)
        editor.apply()
    }

    fun loadTasks(context: Context): MutableList<Pair<String, String>> {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(TASKS_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Pair<String, String>>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
