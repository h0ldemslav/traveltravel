package cz.mendelu.pef.traveltravel.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businesses")
data class BusinessEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo("remote_id")
    var remoteId: String,

    var name: String,

    var category: String,

    var address: String,

    var city: String,

    var url: String,

    var rating: String,

    @ColumnInfo(name = "review_count")
    var reviewCount: String,

    var price: String? = null,

    var latitude: Double,

    var longitude: Double,

    var phone: String,

    @ColumnInfo(name = "user_note")
    var userNote: String? = null,

    @ColumnInfo(name = "when_added")
    var whenAdded: Long,

    @ColumnInfo(name = "is_visited")
    var is_visited: Boolean = false
)
