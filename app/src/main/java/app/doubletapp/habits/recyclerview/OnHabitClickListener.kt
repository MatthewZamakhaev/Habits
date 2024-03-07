package app.doubletapp.habits.recyclerview

interface OnHabitClickListener {

    fun onUpdate(position: Int, model: Habit)

    fun onDelete(model: Habit)

}