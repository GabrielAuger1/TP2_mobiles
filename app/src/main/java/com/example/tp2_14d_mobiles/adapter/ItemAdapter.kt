package com.example.tp2_14d_mobiles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2_14d_mobiles.model.Item
import com.example.tp2_14d_mobiles.databinding.ItemRowBinding

interface OnItemClickListenerInterface {
    fun onItemClick(itemView: View?, position: Int)
    fun onClickEdit(itemView: View, position: Int)
    fun onClickDelete(position: Int)
}

class ItemAdapter(
    private var items: List<Item>,
    private val isAdminMode: Boolean
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

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
                    val edit: android.view.MenuItem = menu.add(0, v.id, 0, "Edit")
                    val delete: android.view.MenuItem = menu.add(0, v.id, 0, "Delete")

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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }
}
