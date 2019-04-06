package com.vp.database.dao

import com.vp.database.model.realmentity.ListItemRealmEntity
import com.vp.database.db.RealmManager
import com.vp.database.extensions.*
import com.vp.database.model.entity.ListItem


class MoviesDAO {

    fun saveFavorite(listItem: ListItem) {
        RealmManager.executeTransaction { realm ->
            realm.saveEntity(mapListItemToRealmEntity(listItem))
        }
    }

    fun removeFavorite(id: String?) {
        RealmManager.executeTransaction { realm ->
            realm.deleteEntity(ListItemRealmEntity::class.java, id?.toInt() ?: 0)
        }
    }

    fun isFavorite(id: String?) = RealmManager.executeTransaction { realm ->
        realm.entityExists(ListItemRealmEntity::class.java, "imdbID", id?.toInt() ?: 0)
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
        imdbID = listItemRealmEntity.imdbID?:""
        poster = listItemRealmEntity.poster?:""
        title = listItemRealmEntity.title?:""
        year = listItemRealmEntity.poster?:""
    }


}