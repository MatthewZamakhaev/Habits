package app.doubletapp.habits.data

import android.content.Context
import android.content.SharedPreferences
import app.doubletapp.habits.recyclerview.Habit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(KEY_SHARED_DATA, Context.MODE_PRIVATE)
    }

    fun saveGoodData(recyclerList: ArrayList<Habit>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(recyclerList)
        editor.putString(KEY_SHARED_LIST1, json)
        editor.apply()
    }

    fun saveBadData(recyclerList: ArrayList<Habit>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(recyclerList)
        editor.putString(KEY_SHARED_LIST2, json)
        editor.apply()
    }

    fun loadGoodData(): ArrayList<Habit> {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_SHARED_LIST1, null)
        val type = object : TypeToken<ArrayList<Habit>>() {}.type
        val recyclerList: ArrayList<Habit> = if (json != null) {
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
        return recyclerList
    }

    fun loadBadData(): ArrayList<Habit> {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_SHARED_LIST2, null)
        val type = object : TypeToken<ArrayList<Habit>>() {}.type
        val recyclerList: ArrayList<Habit> = if (json != null) {
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
        return recyclerList
    }

    companion object {
        private const val KEY_SHARED_DATA = "data"
        private const val KEY_SHARED_LIST1 = "listGood"
        private const val KEY_SHARED_LIST2 = "listBad"
    }
}