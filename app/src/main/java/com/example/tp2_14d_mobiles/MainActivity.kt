package com.example.tp2_14d_mobiles

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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

    //Déclaration des variables

    private lateinit var btnSwitch: SwitchCompat
    private lateinit var item: ItemDao
    private lateinit var binding: ActivityMainBinding
    private lateinit var list: MutableList<Item>
    private lateinit var itemAdapter: ItemAdapter
    var isAdminMode: Boolean = false

    // Fonction appelée lors de la création du Activity.
    // Il initialise , configure l'interface utilisateur

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        item = ItemDatabase.getDatabase(this).itemDao()
        list = mutableListOf()

        itemAdapter = ItemAdapter(list, false)

        val toolbar = binding.toBar.toolbar
        setSupportActionBar(toolbar)
        btnSwitch = binding.toBar.tbar

        val btnPlusAdmin = binding.fab
        btnPlusAdmin.visibility = View.GONE

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        //Navigation entre les fragments
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_panier, R.id.navigation_dashboard)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Fonction qui définit l'activité du bouton ajouter un produit en mode administrateur
        btnPlusAdmin.setOnClickListener { view ->
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Ajouter un produit")
                .setView(dialogView)
                .setPositiveButton("Ajouter") { _, _ ->
                    val itemName = dialogView.findViewById<EditText>(R.id.edit_item_name).text.toString()
                    val itemDescription = dialogView.findViewById<EditText>(R.id.edit_item_description).text.toString()
                    val itemPrice = dialogView.findViewById<EditText>(R.id.edit_item_price).text.toString().toDoubleOrNull() ?: 0.0

                    if (itemName.isNotEmpty() && itemDescription.isNotEmpty()) {
                        thread {
                            val newItem = Item(0, itemName, itemDescription, itemPrice, "autre")
                            val newItemId = item.insertItemReturnId(newItem)

                            runOnUiThread {
                                val newItemWithId = newItem.copy(id = newItemId.toInt())
                                list.add(newItemWithId)
                                itemAdapter.notifyItemInserted(list.size - 1)
                            }
                        }
                    } else {
                        Snackbar.make(view, "Les champs ne peuvent pas être vides.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
                .setNegativeButton("Annuler", null)
                .create()

            dialog.show()
        }

        // Listerner du bouton switch qui bascule entre le mode administrateur et le mode utilisateur.
        // Lorsque le commutateur est modifié, 'isAdmin
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

    //Fonction pour donner de la visibilité au bouton qui ajoute un produit
    fun showFab() {
        binding.fab.visibility = View.VISIBLE
    }

    //Fonction pour cacher le bouton qui ajoute un produit
    fun hideFab() {
        binding.fab.visibility = View.GONE
    }
}
