package com.vp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.database.model.entity.ListItem
import com.vp.list.holder.ListItemViewHolder

class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItems = arrayListOf<ListItem>()
    private lateinit var EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener
    private lateinit var onItemClickListener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false),
            onItemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListItemViewHolder) {
            holder.bindView(listItems[position])
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: List<ListItem>) {
        this.listItems = listItems as ArrayList<ListItem>
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    companion object {
        val NO_IMAGE = "N/A"
    }
}
