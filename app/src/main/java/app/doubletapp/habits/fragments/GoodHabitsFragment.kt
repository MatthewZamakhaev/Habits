package app.doubletapp.habits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.doubletapp.habits.R
import app.doubletapp.habits.data.InstanceAdapterState
import app.doubletapp.habits.data.SharedPreferencesManager
import app.doubletapp.habits.databinding.FragmentGoodHabitsBinding
import app.doubletapp.habits.recyclerview.Habit
import app.doubletapp.habits.recyclerview.HabitAdapter
import app.doubletapp.habits.recyclerview.OnHabitClickListener
import java.util.regex.Pattern

class GoodHabitsFragment : Fragment() {
    private lateinit var binding: FragmentGoodHabitsBinding
    private lateinit var adapter: HabitAdapter
    private lateinit var recyclerList: ArrayList<Habit>
    private var arrayParameter = arrayListOf<String>()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var instanceAdapterState: InstanceAdapterState

    private var colorR = "0"
    private var colorG = "0"
    private var colorB = "0"
    private var colorText = "RGB: (0, 0, 0)"

    private var putKey = "updateItem"
    private var getKey = "newItem"

    private var ids = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoodHabitsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        recyclerList = sharedPreferencesManager.loadGoodData()

        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val manager = LinearLayoutManager(requireContext())

        binding.rvList.layoutManager = manager

        adapter = if (savedInstanceState != null) {
            instanceAdapterState.restoreInstanceState(savedInstanceState, HabitAdapter(habitClickListener, recyclerList))
        } else {
            HabitAdapter(habitClickListener, recyclerList)
        }
        binding.rvList.adapter = adapter
        binding.rvList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        updateData()

        binding.fab.setOnClickListener(onFabClickListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        instanceAdapterState.saveInstanceState(outState, adapter)
    }

    private val onFabClickListener = View.OnClickListener {
        navController.navigate(R.id.newFragment)
    }

    private val habitClickListener = object : OnHabitClickListener {
        override fun onUpdate(position: Int, model: Habit) {
            val id = model.id.toString()
            val name = model.name
            val description = model.description
            val type = model.type
            val priority = model.priority
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
            arrayParameter = arrayListOf(id, name, description, type, priority, frequency, count, colorR, colorG, colorB, colorText)
            updateItem()
        }

        override fun onDelete(model: Habit) {
            adapter.removeHabit(model)
            sharedPreferencesManager.saveGoodData(recyclerList)
        }
    }

    private fun updateData() {
        if (arguments?.isEmpty == false) {
            val arrayItem = arguments?.getStringArrayList(getKey)
            ids = arrayItem!![0].toInt()
            val name = arrayItem[1]
            val description = arrayItem[2]
            val priority = arrayItem[3]
            val type = arrayItem[4]
            val frequency = arrayItem[5]
            colorR = arrayItem[6]
            colorG = arrayItem[7]
            colorB = arrayItem[8]
            colorText = arrayItem[9]

            val model = Habit(
                ids,
                name,
                description,
                priority,
                type,
                frequency,
                colorR,
                colorG,
                colorB,
                colorText
            )

            if (ids == 0) {
                model.id = adapter.getNextItemId()
                adapter.addHabit(model)
            } else {
                adapter.updateHabit(model)
            }

            navController.navigate(R.id.listFragment)

            sharedPreferencesManager.saveGoodData(recyclerList)
        }
    }

    private fun updateItem() {
        val bundle = Bundle().apply {
            putStringArrayList(putKey, arrayParameter)
        }

        navController.navigate(R.id.newFragment, bundle)
    }
}