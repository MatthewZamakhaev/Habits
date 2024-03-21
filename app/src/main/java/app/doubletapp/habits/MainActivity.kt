package app.doubletapp.habits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import app.doubletapp.habits.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setNavigationItemSelectedListener(navItemSelectedListener)

        if (savedInstanceState == null) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.homeFragment)
            binding.navView.setCheckedItem(R.id.nav_main)
        }
    }

    private val navItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.nav_main -> {
                replaceFragment(R.id.listFragment)
            }
            R.id.nav_home -> {
                replaceFragment(R.id.homeFragment)
            }
            R.id.nav_settings -> {
                replaceFragment(R.id.settingsFragment)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        true
    }

    private fun replaceFragment(fragmentId: Int) {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(fragmentId)
    }
}