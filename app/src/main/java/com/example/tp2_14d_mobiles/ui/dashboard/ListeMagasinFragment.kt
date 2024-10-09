package com.example.tp2_14d_mobiles.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
    private var mItem: MutableList<Item> = ArrayList<Item>(0)
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
//        recyclerView = root.findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        val itemDatabase = ItemDatabase.getInstance(requireContext())
//
//        itemDao = itemDatabase.itemDao()
//        itemDao.getAllItems().observe(viewLifecycleOwner, Observer { items ->
//            itemAdapter = ItemAdapter(items, isAdminMode = false)
//            recyclerView.adapter = itemAdapter
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding!!.recyclerView
        val context = recyclerView.context
        recyclerView.layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemAdapter(mItem, isAdminMode = false)
        recyclerView.adapter = itemAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}