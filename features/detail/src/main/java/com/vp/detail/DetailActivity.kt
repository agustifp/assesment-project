package com.vp.detail

import android.graphics.drawable.Drawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel
    private lateinit var starMenuItem: MenuItem
    private var isFav = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        menu?.let {
            starMenuItem = it.findItem(R.id.star)
            starMenuItem.setOnMenuItemClickListener {
                if (isFav) {
                    isFav = false
                    detailViewModel.removeFavorite(queryProvider.getMovieId())
                } else {
                    isFav = true
                    detailViewModel.saveToFavorite(queryProvider.getMovieId())
                }
                changeIconFav(isFav)
                return@setOnMenuItemClickListener true
            }

            isFav = detailViewModel.checkFavorite(queryProvider.getMovieId()) ?: false
            changeIconFav(isFav)
        }

        return true
    }

    private fun changeIconFav(isFav: Boolean) {
        val drawable = when {
            isFav -> ContextCompat.getDrawable(this, R.drawable.ic_star_favorite)
            else -> ContextCompat.getDrawable(this, R.drawable.ic_star)
        }

        starMenuItem.icon = drawable
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
