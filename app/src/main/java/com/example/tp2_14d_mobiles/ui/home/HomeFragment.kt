package com.example.tp2_14d_mobiles.ui.home

import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2_14d_mobiles.databinding.FragmentHomeBinding
import com.example.tp2_14d_mobiles.MainActivity
import com.example.tp2_14d_mobiles.adapter.ItemAdapter
import com.example.tp2_14d_mobiles.data.ItemDao
import com.example.tp2_14d_mobiles.data.ItemDatabase
import com.example.tp2_14d_mobiles.databinding.FragmentListeMagasinBinding
import com.example.tp2_14d_mobiles.model.Item
import com.example.tp2_14d_mobiles.ui.dashboard.ListeMagasinViewModel
import kotlin.time.times


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var listeViewModel: ListeMagasinViewModel
    val listeCompte: MutableList<Triple<Item, Int, Double>> = mutableListOf()



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var itensSelectiones: List<Item>? = null
    private var quantite: List<Int>? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        listeViewModel = ViewModelProvider(requireActivity()).get(ListeMagasinViewModel::class.java)


        itensSelectiones = listeViewModel.selectedItems
        quantite = listeViewModel.quantities

        for(i in 0 until (itensSelectiones?.size ?:0)){
            val item = itensSelectiones?.get(i)

            if(item!=null){
                val qte = quantite!!.get(i)
                val nom = item.nom
                val prix =item.prix
                val prixQte = prix*qte
                val it = Triple(item, qte, prixQte)

                listeCompte.add(it)
            }
        }



        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding!!.itemCompte
        val context = recyclerView.context
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}