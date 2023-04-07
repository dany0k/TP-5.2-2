package ru.vsu.cs.tp.richfamily

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.*
import ru.vsu.cs.tp.richfamily.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Navigation
        binding.bottomNav.bottomNavigationView.background = null
        binding.bottomNav.bottomNavigationView.menu.getItem(2).isEnabled = false
        val bottomNavController = findNavController(R.id.nav_host_fragment_content_main)
        NavigationUI.setupWithNavController(binding.bottomNav.bottomNavigationView, bottomNavController)

        // Fab
        binding.bottomNav.fab.setOnClickListener {

        }

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
                R.id.accountFragment,
                R.id.groupFragment,
                R.id.aboutFragment,
                R.id.creditFragment,
                R.id.addCreditFragment,
                R.id.creditListFragment,
                R.id.reportFragment,
                R.id.categoryFragment
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
}