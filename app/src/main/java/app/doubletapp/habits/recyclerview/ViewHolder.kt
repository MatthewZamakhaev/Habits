package app.doubletapp.habits.recyclerview

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import app.doubletapp.habits.databinding.ItemHabitBinding

class ViewHolder(private val binding: ItemHabitBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(habit: Habit){
        binding.nameHabit.text = habit.name
        binding.descriptionHabit.text = habit.description
        binding.frequencyHabit.text = habit.frequency
        binding.typeHabit.text = habit.type
        binding.priorityHabit.text = habit.priority
        binding.colorHabit.setBackgroundColor(Color.rgb(habit.colorR.toInt(), habit.colorG.toInt(), habit.colorB.toInt()))
        binding.textColorHabit.text = habit.textColor
    }
}