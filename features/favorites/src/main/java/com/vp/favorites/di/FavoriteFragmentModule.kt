package com.vp.favorites.di

import com.vp.favorites.FavoriteFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindFavoriteFragment(): FavoriteFragment
}