package com.example.tp2_14d_mobiles.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2_14d_mobiles.MainActivity
import com.example.tp2_14d_mobiles.adapter.ItemAdapter
import com.example.tp2_14d_mobiles.adapter.OnItemClickListenerInterface
import com.example.tp2_14d_mobiles.data.ItemDao
import com.example.tp2_14d_mobiles.data.ItemDatabase
import com.example.tp2_14d_mobiles.databinding.FragmentListeMagasinBinding
import com.example.tp2_14d_mobiles.model.Item
import com.example.tp2_14d_mobiles.ui.home.HomeViewModel
import kotlin.concurrent.thread

class ListeMagasinFragment : Fragment() {

    private var itemAdapter: ItemAdapter? = null
    private var mItems: MutableList<Item> = ArrayList<Item>(0)
    private var _binding: FragmentListeMagasinBinding? = null
    private val binding get() = _binding!!
    private var isAdminMode: Boolean = false
    private lateinit var liste: ListeMagasinViewModel
    private lateinit var listeViewModel: ListeMagasinViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        liste =
            ViewModelProvider(requireActivity()).get(ListeMagasinViewModel::class.java)

        _binding = FragmentListeMagasinBinding.inflate(inflater, container, false)

        listeViewModel = ViewModelProvider(requireActivity()).get(ListeMagasinViewModel::class.java)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding!!.recyclerView
        val context = recyclerView.context
        isAdminMode = (activity as MainActivity).isAdminMode
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        itemAdapter = ItemAdapter(mItems, isAdminMode )
        recyclerView.adapter = itemAdapter
        itemAdapter!!.onSelectionChangeListener = {
            binding.btnAddToCart.isEnabled = itemAdapter!!.getSelectedItemCount() > 0        }

        binding.btnAddToCart.isEnabled = false

        binding.btnAddToCart.setOnClickListener {
            val selectedItemsWithQuantities = itemAdapter!!.getSelectedItemsWithQuantities()
            addToCart(selectedItemsWithQuantities)

            listeViewModel.selectedItems = selectedItemsWithQuantities.map { it.first }
            listeViewModel.quantities = selectedItemsWithQuantities.map { it.second }

        }

        val itemDao: ItemDao = ItemDatabase.getDatabase(requireContext()).itemDao()
        thread { itemDao?.deleteAllItems() }.join()
        // Populer la base de données
        var item: Item? = Item(1, "Item 1", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", 10.0, "Categorie 1")
        thread { itemDao?.insertItem(item!!) }.join()
        item = Item(2, "Item 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 20.0, "Categorie 2")
        thread { itemDao?.insertItem(item!!) }.join()
        item = Item(3, "Item 3", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", 30.0, "Categorie 3")
        thread { itemDao?.insertItem(item!!) }.join()

        thread {
            mItems = itemDao?.getAllItems()!!
        }.join()

        itemAdapter?.setItems(mItems)

        itemAdapter?.setOnItemClickListener(object : OnItemClickListenerInterface {
            override fun onItemClick(itemView: View?, position: Int) {
                val item = mItems[position]
                liste.setItem(item)
            }

            override fun onClickEdit(itemView: View, position: Int) {
                val item = mItems[position]
            }

            override fun onClickDelete(position: Int) {
                val item = mItems[position]
                thread { itemDao?.deleteItem(item) }.join()
                mItems.removeAt(position)
                itemAdapter?.notifyItemRemoved(position)
            }
        })


    }
    fun addToCart(selectedItems: List<Pair<Item, Int>>) {

        for ((item, quantity) in selectedItems) {
            println("Adding ${item.nom} with quantity $quantity to the cart.")
        }
        Toast.makeText(requireContext(), "${itemAdapter!!.getSelectedItemCount()} items ajoutés au panier!", Toast.LENGTH_SHORT).show()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//    fun cacherFab() {
//        binding.fab.visibility = View.GONE
//    }
}