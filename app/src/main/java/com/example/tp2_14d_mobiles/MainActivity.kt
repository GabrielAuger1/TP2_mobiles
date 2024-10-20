package com.example.tp2_14d_mobiles

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tp2_14d_mobiles.adapter.ItemAdapter
import com.example.tp2_14d_mobiles.data.ItemDao
import com.example.tp2_14d_mobiles.data.ItemDatabase
import com.example.tp2_14d_mobiles.databinding.ActivityMainBinding
import com.example.tp2_14d_mobiles.model.Item
import com.example.tp2_14d_mobiles.ui.liste_magasin.ListeMagasinFragment
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

        // Initialize DAO and list
        item = ItemDatabase.getDatabase(this).itemDao()
        list = mutableListOf()  // Make sure the list is initialized

        itemAdapter = ItemAdapter(list, false)  // Initialize the adapter with the list

        // Set the adapter to your list
        // Your recycler view setup here...

        // Toolbar and Switch setup
        val toolbar = binding.toBar.toolbar
        setSupportActionBar(toolbar)
        btnSwitch = binding.toBar.tbar

        // FAB Setup
        val btnPlusAdmin = binding.fab
        btnPlusAdmin.visibility = View.GONE

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_panier, R.id.navigation_dashboard)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        btnPlusAdmin.setOnClickListener { view ->
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)

            // Create an AlertDialog for adding a new item
            val dialog = AlertDialog.Builder(this)  // Use 'this' in an Activity
                .setTitle("Ajouter un produit")
                .setView(dialogView)
                .setPositiveButton("Ajouter") { _, _ ->
                    // Get user inputs from the dialog
                    val itemName = dialogView.findViewById<EditText>(R.id.edit_item_name).text.toString()
                    val itemDescription = dialogView.findViewById<EditText>(R.id.edit_item_description).text.toString()
                    val itemPrice = dialogView.findViewById<EditText>(R.id.edit_item_price).text.toString().toDoubleOrNull() ?: 0.0

                    // Ensure fields are not empty
                    if (itemName.isNotEmpty() && itemDescription.isNotEmpty()) {
                        // Insert new item into the database in a background thread
                        thread {
                            val newItem = Item(0, itemName, itemDescription, itemPrice, "Default Category")
                            val newItemId = item.insertItemReturnId(newItem)  // Insert the item and get its ID

                            // Update the list and adapter on the UI thread
                            runOnUiThread {
                                val newItemWithId = newItem.copy(id = newItemId.toInt())  // Copy item with the new ID
                                list.add(newItemWithId)  // Add the new item to the list
                                itemAdapter.notifyItemInserted(list.size - 1)  // Notify the adapter that a new item was inserted
                            }
                        }
                    } else {
                        // Optionally show a message if fields are empty
                        Snackbar.make(view, "Les champs ne peuvent pas être vides.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
                .setNegativeButton("Annuler", null)
                .create()

            dialog.show()
        }}


        // Switch logic
        btnSwitch.setOnCheckedChangeListener { _, isChecked ->
            isAdminMode = isChecked
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment

            if (isChecked && currentFragment is ListeMagasinFragment) {
                showFab()
                binding.adm.visibility = View.VISIBLE
            } else {
                hideFab()
                binding.adm.visibility = View.GONE
            }

            itemAdapter.setAdminMode(isAdminMode)
        }
    }

    // Show and Hide FAB
    fun showFab() {
        binding.fab.visibility = View.VISIBLE
    }

    fun hideFab() {
        binding.fab.visibility = View.GONE
    }
}
