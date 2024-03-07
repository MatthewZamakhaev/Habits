package app.doubletapp.habits.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.doubletapp.habits.databinding.ItemHabitBinding

class HabitAdapter(
    private val habitClickListener: OnHabitClickListener,
    private val habits: ArrayList<Habit> = ArrayList()
): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ViewHolder(binding)

        binding.deleteItem.setOnClickListener {
            val position = holder.adapterPosition
            val model = habits[position]
            habitClickListener.onDelete(model)
        }

        binding.updateItem.setOnClickListener {
            val position = holder.adapterPosition
            val model = habits[position]

            habitClickListener.onUpdate(position, model)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    fun addHabit(model: Habit) {
        habits.add(model)
        notifyItemInserted(habits.size)
    }

    fun updateHabit(model: Habit?) {

        if (model == null) return

        for (item in habits) {
            if (item.id == model.id) {
                val position = habits.indexOf(item)
                habits[position] = model
                notifyItemChanged(position)
                break
            }
        }
    }

    fun removeHabit(model: Habit) {
        val position = habits.indexOf(model)
        habits.remove(model)
        notifyItemRemoved(position)
    }

    fun getNextItemId(): Int {
        var id = 1
        if (habits.isNotEmpty()) {
            id = habits.last().id + 1
        }
        return id
    }
}