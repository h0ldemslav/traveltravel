package cz.mendelu.pef.traveltravel.ui.screens.search

import cz.mendelu.pef.traveltravel.R

enum class SearchCategory(val resourceID: Int) {
    Restaurants(R.string.search_screen_category_restaurants),
    Museums(R.string.search_screen_category_museums),
    Parks(R.string.search_screen_category_parks),
    Hotels(R.string.search_screen_category_hotels),
    Hostels(R.string.search_screen_category_hostels)
}

enum class SearchPrice(var value: UInt) {
    VeryLow(1u), Low(2u), Medium(3u), High(4u)
}

class SearchScreenData {
    var name: String = ""
    var category: SearchCategory = SearchCategory.Restaurants
    var prices: MutableMap<SearchPrice, Boolean> = mutableMapOf(
        SearchPrice.VeryLow to true,
        SearchPrice.Low to true,
        SearchPrice.Medium to true,
        SearchPrice.High to true
    )
    var searchError: Int? = null

    fun getAllCategoryResourceIDs(): List<Int> {
        val listOfResourceIDs = mutableListOf(category.resourceID)

        SearchCategory.values().forEach {
            if (it.resourceID != category.resourceID) listOfResourceIDs.add(it.resourceID)
        }

        return listOfResourceIDs.toList()
    }

}