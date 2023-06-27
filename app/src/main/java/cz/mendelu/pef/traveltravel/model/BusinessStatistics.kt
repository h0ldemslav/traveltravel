package cz.mendelu.pef.traveltravel.model

data class BusinessStatistics(
    var citiesWithNumberOfVisitedPlaces: MutableList<Map<String, Int>>,
    var mostVisitedCity: String,
    var favoriteCategory: String,
    var totalNumberOfVisitedPlaces: Int
)

data class StatisticsCity(
    val city: String?,
    val number_of_visited_places: Int
)

data class StatisticsCategory(
    val name: String,
    val number_of_visited_places: Int
)

