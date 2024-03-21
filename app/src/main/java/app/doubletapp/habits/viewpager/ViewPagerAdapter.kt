package app.doubletapp.habits.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.doubletapp.habits.fragments.BadHabitsFragment
import app.doubletapp.habits.fragments.GoodHabitsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GoodHabitsFragment()
            1 -> BadHabitsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}