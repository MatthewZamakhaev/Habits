package app.doubletapp.habits.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import app.doubletapp.habits.R
import app.doubletapp.habits.colorpicker.ColorPickerDialog
import app.doubletapp.habits.databinding.FragmentNewBinding


class NewFragment : Fragment() {
    private lateinit var binding: FragmentNewBinding

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

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

    private var getKey = "updateItem"
    private var putKey = "newItem"

    private var ids = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameText = binding.habitName
        descriptionText = binding.habitDescription
        countText = binding.habitCount
        frequencyText = binding.habitFrequency

        updateItem()

        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        spinner()

        binding.goodRadio.setOnClickListener(radioButtonClickListener())
        binding.badRadio.setOnClickListener(radioButtonClickListener())

        binding.saveFab.setOnClickListener(onFabClickListener)

        binding.colorChange.setOnClickListener(onColorClickListener)
    }

    private fun updateItem(){
        if (arguments?.isEmpty == false) {
            val arrayItem = arguments?.getStringArrayList(getKey)
            ids = arrayItem!![0].toInt()
            val name = arrayItem[1]
            val description = arrayItem[2]
            typeText = arrayItem[3]
            priorityText = arrayItem[4]
            val frequency = arrayItem[5]
            val count = arrayItem[6]
            colorR = arrayItem[7].toInt()
            colorG = arrayItem[8].toInt()
            colorB = arrayItem[9].toInt()

            nameText.setText(name)
            descriptionText.setText(description)
            countText.setText(count)
            frequencyText.setText(frequency)
            binding.colorChange.setBackgroundColor(Color.rgb(colorR, colorG, colorB))

            if(typeText == "Хорошая"){
                binding.goodRadio.isChecked = true
                binding.badRadio.isEnabled = false
            } else if (typeText == "Плохая") {
                binding.badRadio.isChecked = true
                binding.goodRadio.isEnabled = false
            }
        }
    }

    private fun spinner(){
        val spinner = binding.prioritySpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        if(priorityText != ""){
            spinner.setSelection(priorityText.toInt()-1)
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
                Toast.makeText(requireContext(), R.string.habit_priority, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onFabClickListener = View.OnClickListener {
        val name = nameText.text.toString()
        val description = descriptionText.text.toString()
        val priority = priorityText.first().toString()
        val type = typeText
        val frequency = countText.text.toString() + " раз(-а) в " + frequencyText.text.toString() + " дня(-ей)"
        val arrayParameter = arrayListOf(ids.toString(), name, description, priority, type, frequency,
            colorR.toString(), colorG.toString(), colorB.toString(), colorText)

        if (name.isNotBlank() && description.isNotBlank() && priority.isNotBlank()
            && type.isNotBlank() && frequency.isNotBlank()) {

            if (type == "Хорошая") {
                val bundle = Bundle().apply {
                    putStringArrayList(putKey, arrayParameter)
                }

                navController.navigate(R.id.goodHabitsFragment, bundle)
            }

            if (type == "Плохая") {
                val bundle = Bundle().apply {
                    putStringArrayList(putKey, arrayParameter)
                }

                navController.navigate(R.id.badHabitsFragment, bundle)
            }

        } else {
            Toast.makeText(requireContext(), R.string.nothing_change, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val onColorClickListener = View.OnClickListener {
        val colorPickerDialog = ColorPickerDialog(requireContext(), binding.colorChange)
        colorPickerDialog.show { color ->
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            colorR = Color.red(color)
            colorG = Color.green(color)
            colorB = Color.blue(color)
            colorText = "RGB: ($colorR, $colorG, $colorB)"
        }
    }

    private fun radioButtonClickListener(): View.OnClickListener {
        return View.OnClickListener { v ->
            val rb = v as RadioButton
            when (rb.id) {
                R.id.good_radio -> typeText = binding.goodRadio.text.toString()
                R.id.bad_radio -> typeText = binding.badRadio.text.toString()
                else -> {
                    Toast.makeText(requireContext(), R.string.habit_type, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}