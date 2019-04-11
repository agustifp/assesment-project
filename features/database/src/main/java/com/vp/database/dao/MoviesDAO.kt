package com.vp.database.dao

import android.util.Log
import com.vp.database.model.realmentity.ListItemRealmEntity
import com.vp.database.db.RealmManager
import com.vp.database.extensions.*
import com.vp.database.model.entity.MovieItem
import io.realm.exceptions.RealmException


object MoviesDAO {

    fun saveFavorite(movieItem: MovieItem) {
        try {

            RealmManager.executeTransaction { realm ->
                realm.saveEntity(mapListItemToRealmEntity(movieItem))
            }
        } catch (e: RealmException) {
            Log.e("MoviesDao", e.toString())
        }
    }

    fun removeFavorite(id: String) {
        try {
            RealmManager.executeTransaction { realm ->
                realm.deleteEntity(ListItemRealmEntity::class.java, id)
            }
        } catch (e: RealmException) {
            Log.e("MoviesDao", e.toString())
        }
    }

    fun isFavorite(id: String) :Boolean{
        try {
            return RealmManager.executeTransaction { realm ->
                realm.entityExists(ListItemRealmEntity::class.java, "imdbID", id)
            }?:false
        } catch (e: RealmException) {
            Log.e("MoviesDao", e.toString())
        }
        return false
    }

    fun getFavoritesMovies(): List<MovieItem>? {
        try {
            return getFavoritesRealmEntity {
                return@getFavoritesRealmEntity mapRealmEntityList(it)
            }
        } catch (e: RealmException) {
            Log.e("MoviesDao", e.toString())
        }
        return emptyList()
    }


    private fun getFavoritesRealmEntity(block: (List<ListItemRealmEntity>) -> List<MovieItem>?): List<MovieItem>? =
            RealmManager.executeTransaction { realm ->
                realm.getAllEntities(ListItemRealmEntity::class.java) {
                    block(it)
                }
            }


    private fun mapListItemToRealmEntity(movieItem: MovieItem) = ListItemRealmEntity().apply {
        imdbID = movieItem.imdbID
        poster = movieItem.poster
        title = movieItem.title
        year = movieItem.poster
        plot = movieItem.plot
        runtime = movieItem.runtime
        director = movieItem.director
    }

    private fun mapRealmEntityList(realmList: List<ListItemRealmEntity>): List<MovieItem> {
        val domainList = arrayListOf<MovieItem>()
        realmList.forEach {
            domainList.add(mapSingleRealmEntityToDomain(it))
        }
        return domainList.toList()
    }

    private fun mapSingleRealmEntityToDomain(listItemRealmEntity: ListItemRealmEntity) = MovieItem().apply {
        imdbID = listItemRealmEntity.imdbID ?: ""
        poster = listItemRealmEntity.poster ?: ""
        title = listItemRealmEntity.title ?: ""
        year = listItemRealmEntity.poster ?: ""
        plot = listItemRealmEntity.plot ?: ""
        runtime = listItemRealmEntity.runtime ?: ""
        director = listItemRealmEntity.director ?: ""
    }


}