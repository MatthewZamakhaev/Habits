package app.doubletapp.habits

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.doubletapp.habits.databinding.ActivityMainBinding
import app.doubletapp.habits.recyclerview.Habit
import app.doubletapp.habits.recyclerview.HabitAdapter
import app.doubletapp.habits.recyclerview.OnHabitClickListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HabitAdapter
    private lateinit var recyclerList: ArrayList<Habit>
    private var arrayParameter = arrayListOf<String>()

    private var colorR = "0"
    private var colorG = "0"
    private var colorB = "0"
    private var colorText = "RGB: (0, 0, 0)"

    private var id = 0

    private val habitClickListener = object : OnHabitClickListener {
        override fun onUpdate(position: Int, model: Habit) {

            val id = model.id.toString()
            val name = model.name
            val description = model.description
            val type = model.type
            val colorR = model.colorR
            val colorG = model.colorG
            val colorB = model.colorB
            val colorText = model.textColor

            val pattern = Pattern.compile("\\d+")
            val matcher = pattern.matcher(model.frequency)
            val numbers = mutableListOf<String>()

            while (matcher.find()) {
                val number = matcher.group() ?: continue
                numbers.add(number)
            }

            val count = numbers[0]
            val frequency = numbers[1]
            arrayParameter = arrayListOf(id, name, description, type, frequency, count, colorR, colorG, colorB, colorText)
            updateItem()
        }

        override fun onDelete(model: Habit) {
            adapter.removeHabit(model)
            saveData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        val manager = LinearLayoutManager(this)

        binding.rvList.layoutManager = manager
        adapter = HabitAdapter(habitClickListener, recyclerList)
        binding.rvList.adapter = adapter
        binding.rvList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binding.fab.setOnClickListener {
            val intent = Intent(this, NewActivity::class.java)
            startActivity(intent)
            finish()
        }

        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("newItem")) {
                val arrayItem = intent.getStringArrayListExtra("newItem")
                id = arrayItem!![0].toInt()
                val name = arrayItem[1]
                val description = arrayItem[2]
                val priority = arrayItem[3]
                val type = arrayItem[4]
                val frequency = arrayItem[5]
                colorR = arrayItem[6]
                colorG = arrayItem[7]
                colorB = arrayItem[8]
                colorText = arrayItem[9]

                if (arrayItem[0] == "0") {
                    id = adapter.getNextItemId()
                    val model = Habit(id, name, description, priority, type, frequency,
                        colorR, colorG, colorB, colorText)
                    adapter.addHabit(model)
                    saveData()
                } else {
                    val model = Habit(id, name, description, priority, type, frequency,
                        colorR, colorG, colorB, colorText)
                    Log.d("RESULT UP", model.toString())
                    adapter.updateHabit(model)
                    saveData()
                }
            }
        }
    }

    private fun updateItem(){
        val intent = Intent(this, NewActivity::class.java)
        intent.putExtra("updateItem", arrayParameter)
        startActivity(intent)
        finish()
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(recyclerList)
        editor.putString("list", json)
        editor.apply()
    }

    private fun loadData() {
        recyclerList = ArrayList()
        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("list", null)
        val type = object : TypeToken<ArrayList<Habit>>() {}.type

        if (json != null) {
            recyclerList = gson.fromJson(json, type)
        }

    }
}