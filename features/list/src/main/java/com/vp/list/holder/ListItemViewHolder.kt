package com.vp.list.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.database.model.entity.ListItem
import com.vp.list.GlideApp
import com.vp.list.ListAdapter
import com.vp.list.R

class ListItemViewHolder(val view: View, private val onItemClickListener: ListAdapter.OnItemClickListener) : RecyclerView.ViewHolder(view) {

    fun bindView(listItem: ListItem) {
        val image = itemView.findViewById<ImageView>(R.id.poster)
        if (listItem.poster.isNotEmpty() && ListAdapter.NO_IMAGE != listItem.poster) {
            val density = view.resources.displayMetrics.density
            GlideApp
                    .with(image)
                    .load(listItem.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(image)
        } else {
            image.setImageResource(R.drawable.placeholder)
        }

        itemView.setOnClickListener {
            listItem.imdbID.let { it1 -> onItemClickListener.onItemClick(it1) }
        }
    }


}