package app.doubletapp.habits.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.doubletapp.habits.databinding.FragmentListBinding
import app.doubletapp.habits.viewpager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        setupViewPager()
        return binding.root
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> "Хорошие"
                1 -> "Плохие"
                else -> ""
            }
        }.attach()
    }
}