package com.vp.list.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vp.database.model.entity.MovieItem
import com.vp.list.GlideApp
import com.vp.list.ListAdapter
import com.vp.list.R

class ListItemViewHolder(val view: View, private val onItemClickListener: ListAdapter.OnItemClickListener) : RecyclerView.ViewHolder(view) {

    fun bindView(movieItem: MovieItem) {
        val image = itemView.findViewById<ImageView>(R.id.poster)
        val title = itemView.findViewById<TextView>(R.id.title)
        if (movieItem.poster.isNotEmpty() && ListAdapter.NO_IMAGE != movieItem.poster) {
            image.visibility = View.VISIBLE
            val density = view.resources.displayMetrics.density
            GlideApp
                    .with(image)
                    .load(movieItem.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(image)
            title.text = movieItem.title
        } else {
            image.visibility = View.GONE
        }

        itemView.setOnClickListener {
            movieItem.imdbID.let { it1 -> onItemClickListener.onItemClick(it1) }
        }
    }


}