package com.example.tp2_14d_mobiles.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2_14d_mobiles.R
import com.example.tp2_14d_mobiles.databinding.FragmentHomeBinding
import com.example.tp2_14d_mobiles.model.Item
import com.example.tp2_14d_mobiles.ui.dashboard.ListeMagasinViewModel



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var listeViewModel: ListeMagasinViewModel
    val listeCompte: MutableList<Triple<Item, Double, Double>> = mutableListOf()
    var tvq: Double =0.0
    var tps: Double =0.0
    var total: Double =0.0
    var some: Double =0.0
    var prixQte: Double = 0.0


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
                var qte = quantite!!.get(i).toDouble()
                var nom = item.nom
                var prix =item.prix
                prixQte = (prix*qte)
                var it = Triple(item, qte, prixQte)

                listeCompte.add(it)

            }
            some += prixQte

        }

        tps = (some*0.05)
        tvq = (some*0.0997)
        total = tps+tvq+some

        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val recyclerView: RecyclerView = binding!!.itemCompte
        //val context = recyclerView.context
        //recyclerView.layoutManager = LinearLayoutManager(context)
       // recyclerView.setHasFixedSize(true)

        val linearLayout: LinearLayout = binding.itemCompte
        linearLayout.removeAllViews()
        val totalPanier: LinearLayout = binding.totalPanier
        totalPanier.visibility =View.GONE
        if(listeCompte.isNotEmpty()){
            for (item in listeCompte) {
                val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_compte, linearLayout, false)

                val nom: TextView = itemView.findViewById(R.id.nom)
                val qte: TextView = itemView.findViewById(R.id.qte)
                val subTotal: TextView = itemView.findViewById(R.id.sub_total)

                nom.text = item.first.nom
                qte.text = item.second.toString()
                subTotal.text = item.third.toString()


                linearLayout.addView(itemView)

            }

            totalPanier.visibility= View.VISIBLE
            val tvq: TextView = totalPanier.findViewById(R.id.tqv)
            val tps: TextView = totalPanier.findViewById(R.id.tps)
            val tot: TextView = totalPanier.findViewById(R.id.total_compte)

            //tvq.text = String.format("%.2f", tvq.toFloat())
           // tps.text = String.format("%.2f", tps.toFloat())
            tot.text = String.format("%.2f", total.toFloat())
            //tvq.text = tvq.toString()
           // tps.text = tps.toString()
           // tot.text = total.toString()



        }else{
            totalPanier.visibility =View.GONE
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}