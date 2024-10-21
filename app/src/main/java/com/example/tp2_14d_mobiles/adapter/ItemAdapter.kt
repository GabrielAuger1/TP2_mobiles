package com.example.tp2_14d_mobiles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2_14d_mobiles.model.Item
import com.example.tp2_14d_mobiles.databinding.ItemRowBinding

//Fonctions appelées en mode administrateur
interface OnItemClickListenerInterface {
    fun onItemClick(itemView: View?, position: Int)
    fun onClickEdit(itemView: View, position: Int)
    fun onClickDelete(position: Int)
}

// Adapter pour le RecyclerView qui gère l'affichage des éléments de la liste.
// Permet de basculer entre les modes administrateur et utilisateur et gère la sélection des éléments.
class ItemAdapter(
    private var items: List<Item>,
    private var isAdminMode: Boolean
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var onSelectionChangeListener: (() -> Unit)? = null

    private val itemQuantities: MutableMap<Int, Int> = mutableMapOf()
    private val selectedItems: MutableSet<Int> = mutableSetOf()

    lateinit var listener: OnItemClickListenerInterface

    fun setOnItemClickListener(listener: OnItemClickListenerInterface) {
        this.listener = listener
    }

    inner class ViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, adapterPosition)
                }
            }

            if (isAdminMode) {
                binding.root.setOnCreateContextMenuListener { menu, v, menuInfo ->
                    val position = adapterPosition
                    val edit: android.view.MenuItem = menu.add(0, v.id, 0, "Modifier")
                    val delete: android.view.MenuItem = menu.add(0, v.id, 0, "Supprimer")

                    edit.setOnMenuItemClickListener {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClickEdit(itemView, position)
                        }
                        false
                    }

                    delete.setOnMenuItemClickListener {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClickDelete(position)
                        }
                        false
                    }
                }
            }
        }

        fun bind(item: Item) {
            binding.itemName.text = item.nom
            binding.itemDescription.text = item.description
            binding.itemPrice.text = item.prix.toString()

            val quantity = itemQuantities[item.id] ?: 1
            binding.tvQuantity.text = quantity.toString()

            updateDecrementButtonState(quantity)

            binding.btnIncrement.setOnClickListener {
                val newQuantity = (itemQuantities[item.id] ?: 1) + 1
                itemQuantities[item.id] = newQuantity
                binding.tvQuantity.text = newQuantity.toString()
                updateDecrementButtonState(newQuantity)
            }

            binding.btnDecrement.setOnClickListener {
                val currentQuantity = itemQuantities[item.id] ?: 1
                if (currentQuantity > 1) {
                    val newQuantity = currentQuantity - 1
                    itemQuantities[item.id] = newQuantity
                    binding.tvQuantity.text = newQuantity.toString()
                    updateDecrementButtonState(newQuantity)
                }
            }

            binding.checkboxSelectItem.setOnCheckedChangeListener(null)
            binding.checkboxSelectItem.isChecked = selectedItems.contains(item.id)

            binding.checkboxSelectItem.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(item.id)
                } else {
                    selectedItems.remove(item.id)
                }
                onSelectionChangeListener?.invoke()  // Notify when selection changes
            }
        }

        private fun updateDecrementButtonState(quantity: Int) {
            binding.btnDecrement.isEnabled = quantity > 1
        }

    }

    // Fonction pour créer et renvoier une nouvelle instance de ViewHolder,
    // affiche la disposition des éléments de liste
    // permet l'accès pendant le processus de liaison.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Méthode appelée par RecyclerView pour afficher les données à la position spécifiée
    // et pour la mise à jour de l'interface utilisateur.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    //// Mis à jour de la liste des éléments affichés par l'adaptateur.
    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    // Renvoie le nombre total d'éléments sélectionnés
    fun getSelectedItemCount(): Int {
        return selectedItems.sumOf { itemId ->
            itemQuantities[itemId] ?: 1
        }
    }

    // Renvoie la liste de paires contenant les éléments sélectionnés et leurs quantités respectives.
    fun getSelectedItemsWithQuantities(): List<Pair<Item, Int>> {
        return items.filter { selectedItems.contains(it.id) }
            .map { item -> item to (itemQuantities[item.id] ?: 1) }
    }

    //Définit le mode Administrateur à l’aide du bouton switch
    fun setAdminMode(isAdminMode: Boolean) {
        this.isAdminMode = isAdminMode
        notifyDataSetChanged()
    }

}
