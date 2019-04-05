package com.vp.database.model.realmentity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class ListItemRealmEntity : RealmObject() {
    @PrimaryKey
    var imdbID: String? = null
    var title: String? = null
    var year: String? = null
    var poster: String? = null
}
