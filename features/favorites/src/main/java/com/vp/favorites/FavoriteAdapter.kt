package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.vp.database.model.entity.ListItem
import com.vp.list.GlideApp

import java.util.Collections
import androidx.recyclerview.widget.RecyclerView

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    private var listItems: MutableList<ListItem> = mutableListOf()

    private lateinit var EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]
        if (listItem.poster.isNotEmpty() && NO_IMAGE != listItem.poster) {
            GlideApp
                    .with(holder.image)
                    .load(listItem.poster)
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: List<ListItem>) {
        this.listItems.addAll(listItems)
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    companion object {
        private val NO_IMAGE = "N/A"
    }
}