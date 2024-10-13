package com.example.tp2_14d_mobiles

import android.os.Bundle
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tp2_14d_mobiles.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var btnSwitch: SwitchCompat

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Déclaration du Toolbar
        val toolbar = binding.toBar.toolbar
        setSupportActionBar(toolbar)

        // Déclaration du bouton switch
        btnSwitch = binding.toBar.tbar

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // Actions sur les items du bouton switch
        btnSwitch.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked) {
                setContentView(R.layout.main_admin)



            }else{

                setContentView(binding.root)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (btnSwitch.isChecked) {
            menuInflater.inflate(R.menu.menu_admin, menu)
            return true
        }
        return false
    }


}