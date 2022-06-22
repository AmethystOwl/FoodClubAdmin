package com.example.foodyadminpanel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.foodyadminpanel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.findNavController()!!
        setSupportActionBar(binding.toolbar)
        val appBarConfig = AppBarConfiguration(setOf(R.id.loginFragment,R.id.homeFragment))
        NavigationUI.setupWithNavController(binding.toolbar,navController,appBarConfig)

    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}
