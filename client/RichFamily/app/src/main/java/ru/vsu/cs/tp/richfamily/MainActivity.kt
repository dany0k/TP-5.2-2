package ru.vsu.cs.tp.richfamily

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.ui.*
import ru.vsu.cs.tp.richfamily.databinding.ActivityMainBinding
import ru.vsu.cs.tp.richfamily.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.root)
        // Navigation
        binding.bottomNav.bottomNavigationView.background = null
        val bottomNavController = findNavController(R.id.nav_host_fragment_content_main)
        NavigationUI.setupWithNavController(binding.bottomNav.bottomNavigationView, bottomNavController)
        // Action bar
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.walletFragment,
                R.id.templateFragment,
                R.id.consumptionFragment,
                R.id.incomeFragment,
                R.id.creditListFragment,
                R.id.categoryFragment,
                R.id.reportFragment,
                R.id.groupListFragment,
                R.id.accountFragment,
                R.id.aboutFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        private lateinit var instance: MainActivity
        fun getToken(): String {
            val token = try {
                SessionManager.getToken(instance)!!
            } catch (e: java.lang.NullPointerException) {
                ""
            }
            return token
        }
    }
}