package com.example.tp2_14d_mobiles.ui.panier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tp2_14d_mobiles.MainActivity
import com.example.tp2_14d_mobiles.R
import com.example.tp2_14d_mobiles.databinding.FragmentPanierBinding
import com.example.tp2_14d_mobiles.model.Item
import com.example.tp2_14d_mobiles.ui.liste_magasin.ListeMagasinViewModel

class PanierFragment : Fragment() {

    //Déclaration des variables
    private var _binding: FragmentPanierBinding? = null
    private lateinit var listeViewModel: ListeMagasinViewModel
    val listeCompte: MutableList<Triple<Item, Int, Double>> = mutableListOf()
    var tvq: Double = 0.0
    var tps: Double = 0.0
    var total: Double = 0.0
    var some: Double = 0.0
    var prixQte: Double = 0.0

    private val binding get() = _binding!!
    private var itensSelectiones: List<Item>? = null
    private var quantite: List<Int>? = null

    //Fonction pour créer et afficher  le layout du fragment
    //lorsqu'il est affiché dans l'interface utilisateur.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        listeViewModel = ViewModelProvider(requireActivity()).get(ListeMagasinViewModel::class.java)

        itensSelectiones = listeViewModel.selectedItems
        quantite = listeViewModel.quantities

        initializeCart()

        _binding = FragmentPanierBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initializeCart() {
        listeCompte.clear()

        for (i in 0 until (itensSelectiones?.size ?: 0)) {
            val item = itensSelectiones?.get(i)

            if (item != null) {
                val qte = quantite!!.get(i).toInt()
                val prix = item.prix
                prixQte = (prix * qte)
                val cartItem = Triple(item, qte, prixQte)

                listeCompte.add(cartItem)
                some += prixQte
            }
        }

        tps = (some * 0.05)
        tvq = (some * 0.0997)
        total = tps + tvq + some
    }

    // Fonction appelée juste après la création du fragment Panier.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateCartUI()
    }

    //Fonction appelée lorsque la vue du fragment est détruite
    // pour assurer que la référence à _binding est définie sur null
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Initialise des composants et configure des comportements d'interface.
    private fun updateCartUI() {
        val linearLayout: LinearLayout = binding.itemCompte
        linearLayout.removeAllViews()

        if (listeCompte.isNotEmpty()) {
            for (item in listeCompte) {
                val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_compte, linearLayout, false)
                val nom: TextView = itemView.findViewById(R.id.nom)
                val qte: TextView = itemView.findViewById(R.id.qte)
                val subTotal: TextView = itemView.findViewById(R.id.sub_total)

                nom.text = item.first.nom
                qte.text = item.second.toString()
                subTotal.text = String.format("%.2f", item.third)

                linearLayout.addView(itemView)
            }

            binding.totalPanier.visibility = View.VISIBLE
            binding.totalPanier.findViewById<TextView>(R.id.tqv).text = String.format("%.2f", tvq)
            binding.totalPanier.findViewById<TextView>(R.id.tps).text = String.format("%.2f", tps)
            binding.totalPanier.findViewById<TextView>(R.id.total_compte).text = String.format("%.2f", total)
        } else {
            binding.totalPanier.visibility = View.GONE
        }
    }

    //Méthode appelée lorsque le Fragment revient à l'état actif et visible par l'utilisateur.
    //Cela permet de contrôler la visibilité du FAB (floating action button) en fonction de l'état du Fragment.
    override fun onResume() {
        super.onResume()

        val mainActivity = activity as? MainActivity
        mainActivity?.hideFab()
    }
}
