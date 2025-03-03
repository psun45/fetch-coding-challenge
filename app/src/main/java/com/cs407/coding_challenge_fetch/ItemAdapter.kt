package com.cs407.coding_challenge_fetch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private var items: List<Item> = listOf()

    fun setData(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listIdTextView: TextView = itemView.findViewById(R.id.listIdTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(item: Item) {
            listIdTextView.text = "List ID: ${item.listId}"
            nameTextView.text = "Name: ${item.name}"
        }
    }
}