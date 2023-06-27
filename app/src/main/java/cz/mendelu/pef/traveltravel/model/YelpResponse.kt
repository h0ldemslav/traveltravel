package cz.mendelu.pef.traveltravel.model

data class YelpResponse(
    val businesses: List<Business>,
    val total: String,
    val region: Region
)

data class Business(
    val id: String,
    val alias: String,
    val name: String,
    val image_url: String,
    val is_closed: Boolean,
    val url: String,
    val review_count: String,
    val categories: List<Category>,
    val rating: String,
    val coordinates: Center,
    val transactions: List<String>,
    val price: String?,
    val location: Location,
    val phone: String,
    val display_phone: String,
    val distance: String? = null,
    val hours: List<BusinessHours>?
)

data class Category(
    val alias: String,
    val title: String
)

data class Center(
    val latitude: String,
    val longitude: String
)

data class Location(
    val address1: String? = null,
    val address2: String? = null,
    val address3: String? = null,
    val city: String? = null,
    val zip_code: String? = null,
    val country: String? = null,
    val state: String? = null,
    val display_address: List<String>,
    val cross_streets: String?
)

data class Region(
    val center: Center
)

data class BusinessHours(
    val hour_type: String,
    val open: List<OpenHours>,
    val is_open_now: Boolean
)

data class OpenHours(
    val day: Int,
    val start: String,
    val end: String,
    val is_overnight: Boolean,
)