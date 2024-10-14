package com.example.tp2_14d_mobiles

import android.os.Bundle
import android.widget.Button
import com.example.tp2_14d_mobiles.data.ItemDao
import com.example.tp2_14d_mobiles.data.ItemDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tp2_14d_mobiles.databinding.ActivityMainBinding
import com.example.tp2_14d_mobiles.model.Item
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var btnSwitch: SwitchCompat
    private lateinit var itemDao: ItemDao
    private lateinit var binding: ActivityMainBinding
    private lateinit var list: MutableList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemDao = ItemDatabase.getDatabase(this).itemDao()


        //Déclaration du Toolbar
        val toolbar = binding.toBar.toolbar
        setSupportActionBar(toolbar)

        // Déclaration du bouton switch
        btnSwitch = binding.toBar.tbar

        val btnPlusAdmin = findViewById<Button>(R.id.btn_plus_admin)

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

        btnPlusAdmin.setOnClickListener { view ->

            Snackbar.make(view, "Produit ajouté", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            // Ajout d'un produit en BD quand on clique sur le bouton

            thread {
                ItemDao.insertItem(Item(0,"Produit", "Present surprise", 15.2, "cadeau"))
            }.join()
        }

        // Actions sur les items du bouton switch
        btnSwitch.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked) {
                setContentView(R.layout.main_admin)



            }else{

                setContentView(binding.root)
            }
        }

    }



}