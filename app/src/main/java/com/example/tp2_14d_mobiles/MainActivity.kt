package com.example.tp2_14d_mobiles

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2_14d_mobiles.adapter.ItemAdapter
import com.example.tp2_14d_mobiles.data.ItemDao
import com.example.tp2_14d_mobiles.data.ItemDatabase
import com.example.tp2_14d_mobiles.databinding.ActivityMainBinding
import com.example.tp2_14d_mobiles.model.Item
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var btnSwitch: SwitchCompat
    private lateinit var item: ItemDao
    private lateinit var binding: ActivityMainBinding
    private lateinit var list: MutableList<Item>
    private lateinit var itemAdapter: ItemAdapter
    var isAdminMode: Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        item = ItemDatabase.getDatabase(this).itemDao()

        val txAdm = binding.adm
        txAdm.visibility = View.GONE


        itemAdapter = ItemAdapter(listOf(), false)


        //Déclaration du Toolbar
        val toolbar = binding.toBar.toolbar
        setSupportActionBar(toolbar)

        // Déclaration du bouton switch
        btnSwitch = binding.toBar.tbar

        val btnPlusAdmin = binding.fab
        btnPlusAdmin.visibility = View.GONE

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

            itemAdapter?.setItems(list)

            thread {
                item.insertItem(Item(0,"Produit", "Present surprise", 15.2, "cadeau"))
            }.join()
            itemAdapter?.notifyItemInserted (list.size-1)
        }

        // Actions sur les items du bouton switch
        btnSwitch.setOnCheckedChangeListener { _, isChecked ->
            isAdminMode = isChecked
            if(isChecked) {

                btnPlusAdmin.visibility = View.VISIBLE
                txAdm.visibility = View.VISIBLE

                itemAdapter.setAdminMode(isAdminMode)
                isAdminMode=true




            }else{

                btnPlusAdmin.visibility = View.GONE
                txAdm.visibility = View.GONE
                isAdminMode=false
            }
        }



    }



}


