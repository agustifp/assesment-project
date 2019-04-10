package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel
    private lateinit var starMenuItem: MenuItem
    private var isFav = false
    private lateinit var movieIdIntent:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMovieIdFromIntent()
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails(movieIdIntent)
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
                    detailViewModel.removeFavorite()
                } else {
                    isFav = true
                    detailViewModel.saveToFavorite()
                }
                changeIconFav(isFav)
                return@setOnMenuItemClickListener true
            }

            isFav = detailViewModel.checkFavorite()
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

    private fun getMovieIdFromIntent() {
        movieIdIntent = intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }


}
