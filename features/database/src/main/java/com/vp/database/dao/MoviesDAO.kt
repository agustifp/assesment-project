package com.vp.database.dao

import android.util.Log
import com.vp.database.model.realmentity.ListItemRealmEntity
import com.vp.database.db.RealmManager
import com.vp.database.extensions.*
import com.vp.database.model.entity.ListItem
import io.realm.exceptions.RealmException


object MoviesDAO {

    fun saveFavorite(listItem: ListItem) {
        try {

            RealmManager.executeTransaction { realm ->
                realm.saveEntity(mapListItemToRealmEntity(listItem))
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

    fun getFavoritesMovies(): List<ListItem>? {
        try {
            return getFavoritesRealmEntity {
                return@getFavoritesRealmEntity mapRealmEntityList(it)
            }
        } catch (e: RealmException) {
            Log.e("MoviesDao", e.toString())
        }
        return emptyList()
    }


    private fun getFavoritesRealmEntity(block: (List<ListItemRealmEntity>) -> List<ListItem>?): List<ListItem>? =
            RealmManager.executeTransaction { realm ->
                realm.getAllEntities(ListItemRealmEntity::class.java) {
                    block(it)
                }
            }


    private fun mapListItemToRealmEntity(listItem: ListItem) = ListItemRealmEntity().apply {
        imdbID = listItem.imdbID
        poster = listItem.poster
        title = listItem.title
        year = listItem.poster
    }

    private fun mapRealmEntityList(realmList: List<ListItemRealmEntity>): List<ListItem> {
        val domainList = arrayListOf<ListItem>()
        realmList.forEach {
            domainList.add(mapSingleRealmEntityToDomain(it))
        }
        return domainList.toList()
    }

    private fun mapSingleRealmEntityToDomain(listItemRealmEntity: ListItemRealmEntity) = ListItem().apply {
        imdbID = listItemRealmEntity.imdbID ?: ""
        poster = listItemRealmEntity.poster ?: ""
        title = listItemRealmEntity.title ?: ""
        year = listItemRealmEntity.poster ?: ""
    }


}