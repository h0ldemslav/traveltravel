package cz.mendelu.pef.traveltravel.model

data class BusinessData(
    var id: Long? = null,

    var remoteId: String,

    var name: String,

    var category: String,

    var address: String,

    var city: String,

    var url: String,

    var rating: String,

    var reviewCount: String,

    var price: String? = null,

    var latitude: Double,

    var longitude: Double,

    var phone: String,

    var userNote: String? = null,

    var whenAdded: Long = 0L,

    var is_visited: Boolean = false
)
