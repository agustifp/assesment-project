package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.vp.database.model.entity.MovieItem
import com.vp.list.GlideApp

import androidx.recyclerview.widget.RecyclerView

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    private var listItems = listOf<MovieItem>()

    private lateinit var EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movieItem = listItems[position]
        if (movieItem.poster.isNotEmpty() && NO_IMAGE != movieItem.poster) {
            GlideApp
                    .with(holder.image)
                    .load(movieItem.poster)
                    .into(holder.image)
            holder.title.text = movieItem.title
        } else {
            holder.image.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(movieItems: List<MovieItem>) {
        this.listItems = movieItems
        notifyDataSetChanged()
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
        var title: TextView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
            title = itemView.findViewById(R.id.title)
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