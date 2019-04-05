package com.vp.movies.database.dao

import com.vp.list.model.ListItem
import com.vp.movies.database.model.realmentity.ListItemRealmEntity
import com.vp.movies.database.db.RealmManager
import com.vp.movies.database.extensions.getAllEntities
import com.vp.movies.database.extensions.saveEntity


class MoviesDAO {

    fun saveFavorite(listItem: ListItem) {
        RealmManager.executeTransaction { realm ->
            realm.saveEntity(mapListItemToRealmEntity(listItem))
        }
    }

    fun getFavoritesMovies() =
            getFavoritesRealmEntity {
                return@getFavoritesRealmEntity mapRealmEntityList(it)
            }


    private fun getFavoritesRealmEntity(block: (List<ListItemRealmEntity>) -> List<ListItem>?): List<ListItem>? =
            RealmManager.executeTransaction { realm ->
                realm.getAllEntities(ListItemRealmEntity::class.java) {
                    block(it)
                }
            }


    private fun mapListItemToRealmEntity(listItem: ListItem) = ListItemRealmEntity().apply {
        imdbID = listItem.imdbID.toInt()
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
        imdbID = listItemRealmEntity.imdbID.toString()
        poster = listItemRealmEntity.poster
        title = listItemRealmEntity.title
        year = listItemRealmEntity.poster
    }


}