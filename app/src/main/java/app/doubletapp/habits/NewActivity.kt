package app.doubletapp.habits

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.doubletapp.habits.colorpicker.ColorPickerDialog
import app.doubletapp.habits.databinding.ActivityNewBinding


class NewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewBinding

    private lateinit var nameText: EditText
    private lateinit var descriptionText: EditText
    private var typeText = ""
    private var priorityText = ""
    private lateinit var countText: EditText
    private lateinit var frequencyText: EditText

    private var colorR = 0
    private var colorG = 0
    private var colorB = 0
    private var colorText = "RGB: (0, 0, 0)"

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameText = binding.habitName
        descriptionText = binding.habitDescription
        countText = binding.habitCount
        frequencyText = binding.habitFrequency

        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("updateItem")) {
                val arrayItem = intent.getStringArrayListExtra("updateItem")
                id = arrayItem!![0].toInt()
                val name = arrayItem[1]
                val description = arrayItem[2]
                val frequency = arrayItem[4]
                val count = arrayItem[5]
                colorR = arrayItem[6].toInt()
                colorG = arrayItem[7].toInt()
                colorB = arrayItem[8].toInt()

                nameText.setText(name)
                descriptionText.setText(description)
                countText.setText(count)
                frequencyText.setText(frequency)
                binding.colorChange.setBackgroundColor(Color.rgb(colorR, colorG, colorB))
            }
        }

        val spinner = binding.prioritySpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.priority_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.priority_array)
                priorityText = choose[selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@NewActivity, "Выберите приоритет привычки", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goodRadio.setOnClickListener(radioButtonClickListener())
        binding.badRadio.setOnClickListener(radioButtonClickListener())
        binding.neutralRadio.setOnClickListener(radioButtonClickListener())

        binding.saveFab.setOnClickListener {

            val name = nameText.text.toString()
            val description = descriptionText.text.toString()
            val priority = priorityText.first().toString()
            val type = typeText
            val frequency = countText.text.toString() + " раз(-а) в " + frequencyText.text.toString() + " дня(-ей)"
            val arrayParameter = arrayListOf(id.toString(), name, description, priority, type, frequency,
                colorR.toString(), colorG.toString(), colorB.toString(), colorText)

            if (name.isNotBlank() && description.isNotBlank() && priority.isNotBlank()
                && type.isNotBlank() && frequency.isNotBlank()) {

                nameText.setText("")
                descriptionText.setText("")
                countText.setText("")
                frequencyText.setText("")

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("newItem", arrayParameter)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this@NewActivity, "Укажите недостающие данные", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.colorChange.setOnClickListener {
            val colorPickerDialog = ColorPickerDialog(this, binding.colorChange)
            colorPickerDialog.show { color ->
                val hsv = FloatArray(3)
                Color.colorToHSV(color, hsv)
                colorR = Color.red(color)
                colorG = Color.green(color)
                colorB = Color.blue(color)
                colorText = "RGB: ($colorR, $colorG, $colorB)"
            }
        }
    }

    private fun radioButtonClickListener(): View.OnClickListener {
        return View.OnClickListener { v ->
            val rb = v as RadioButton
            when (rb.id) {
                R.id.good_radio -> typeText = binding.goodRadio.text.toString()
                R.id.bad_radio -> typeText = binding.badRadio.text.toString()
                R.id.neutral_radio -> typeText = binding.neutralRadio.text.toString()
                else -> {
                    Toast.makeText(this@NewActivity, "Выберите тип привычки", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}