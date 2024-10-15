package com.example.tp2_14d_mobiles.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2_14d_mobiles.adapter.ItemAdapter
import com.example.tp2_14d_mobiles.data.ItemDao
import com.example.tp2_14d_mobiles.data.ItemDatabase
import com.example.tp2_14d_mobiles.databinding.FragmentListeMagasinBinding
import com.example.tp2_14d_mobiles.model.Item
import kotlin.concurrent.thread

class ListeMagasinFragment : Fragment() {

    private var itemAdapter: ItemAdapter? = null
    private var mItems: MutableList<Item> = ArrayList<Item>(0)
    private var _binding: FragmentListeMagasinBinding? = null
    private val binding get() = _binding!!
    private lateinit var liste: ListeMagasinViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        liste =
            ViewModelProvider(requireActivity()).get(ListeMagasinViewModel::class.java)

        _binding = FragmentListeMagasinBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding!!.recyclerView
        val context = recyclerView.context
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        itemAdapter = ItemAdapter(mItems, isAdminMode = false)
        recyclerView.adapter = itemAdapter
        val itemDao: ItemDao = ItemDatabase.getDatabase(requireContext()).itemDao()
        thread { itemDao?.deleteAllItems() }.join()
        var item: Item? = Item(1, "Item 1", "Description 1", 10.0, "Categorie 1")
        thread { itemDao?.insertItem(item!!) }.join()
        item = Item(2, "Item 2", "Description 2", 20.0, "Categorie 2")
        thread { itemDao?.insertItem(item!!) }.join()
        item = Item(3, "Item 3", "Description 3", 30.0, "Categorie 3")
        thread { itemDao?.insertItem(item!!) }.join()

        thread {
            mItems = itemDao?.getAllItems()!!
        }.join()

        itemAdapter?.setItems(mItems)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
   // fun cacherFab() {
    //    binding.fab.visibility = View.GONE
   // }
}