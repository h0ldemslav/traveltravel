package cz.mendelu.pef.traveltravel.ui.screens.search

import cz.mendelu.pef.traveltravel.model.Business

interface SearchScreenActions {
    fun sendAPIRequest(offset: Int, locale: String)
    fun loadNextPage(offset: Int)
    fun validateBeforeSearch()
    fun updateNameFieldValue(value: String)
    fun updateCategoryFieldValue(resourceID: Int)
    fun updatePriceFieldValue(value: SearchPrice, isChecked: Boolean)
    fun saveToDataStore(business: Business, category: String)
}