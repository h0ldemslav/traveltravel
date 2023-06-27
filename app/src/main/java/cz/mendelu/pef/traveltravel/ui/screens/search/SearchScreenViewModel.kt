package cz.mendelu.pef.traveltravel.ui.screens.search

import androidx.compose.runtime.*
import com.google.gson.Gson
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.architecture.BaseViewModel
import cz.mendelu.pef.traveltravel.model.Business
import cz.mendelu.pef.traveltravel.model.YelpResponse
import cz.mendelu.pef.traveltravel.localstore.LocalStoreManager
import cz.mendelu.pef.traveltravel.model.BusinessData
import cz.mendelu.pef.traveltravel.navigation.otherCity
import cz.mendelu.pef.traveltravel.services.api.MAX_OFFSET
import cz.mendelu.pef.traveltravel.services.api.YelpAPIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchScreenViewModel(
    private val apiRepository: YelpAPIRepository,
    private val localStoreManager: LocalStoreManager
) : BaseViewModel(), SearchScreenActions {

    var data: SearchScreenData = SearchScreenData()
    var placeholderData: SearchScreenPlaceholderData = SearchScreenPlaceholderData()
    var state: MutableState<SearchScreenUIState> = mutableStateOf(SearchScreenUIState.Default)
    var businesses = mutableStateListOf<Business>()

    override fun saveToDataStore(business: Business, category: String) {
        var address = ""

        business.location.display_address.forEachIndexed { index, addressPart ->
            address += addressPart
            if (index != business.location.display_address.lastIndex) address += ", "
        }

        val city = business.location.city ?: otherCity
        val latitude = business.coordinates.latitude.toDouble()
        val longitude = business.coordinates.longitude.toDouble()

        val businessData = BusinessData(
            remoteId = business.id,
            name = business.name,
            category = category,
            address = address,
            city = city,
            url = business.url,
            rating = business.rating,
            reviewCount = business.review_count,
            price = business.price,
            latitude = latitude,
            longitude = longitude,
            phone = business.display_phone,
        )

        val gson = Gson()
        val jsonString = gson.toJson(businessData, BusinessData::class.java)

        launch {
            localStoreManager.saveTemporaryBusinessToStore(jsonString)
        }
    }

    override fun sendAPIRequest(offset: Int, locale: String) {
        placeholderData.clearPlaceholder()

        val prices: List<Int> = data.prices
            .filter { it.value }
            .map { it.key.value.toInt() }

        launch {
            val response = apiRepository.getBusinesses(
                locationName = data.name,
                category = data.category.name.lowercase(),
                prices = prices,
                offset = offset,
                locale = locale
            )

            if (response != null) {
                processResponse(response = response, offset = offset)
            } else {
                placeholderData.setNoInternetPlaceholder()
                state.value = SearchScreenUIState.Error
            }
        }
    }

    override fun loadNextPage(offset: Int) {
        if (state.value != SearchScreenUIState.PageEnd && offset < MAX_OFFSET) {
            state.value = SearchScreenUIState.Paginating(offset = offset)
        }
    }

    override fun validateBeforeSearch() {
        if (data.name.isEmpty()) {
            data.searchError = R.string.search_textfield_name_error
            state.value = SearchScreenUIState.DataChanged
        } else if (data.prices.values.find { it } == null) {
            data.searchError = R.string.search_price_checkbox_error
            state.value = SearchScreenUIState.DataChanged
        } else {
            data.searchError = null
            state.value = SearchScreenUIState.InitialLoading
        }
    }

    override fun updateNameFieldValue(value: String) {
        data.name = value
        state.value = SearchScreenUIState.DataChanged
    }

    override fun updateCategoryFieldValue(resourceID: Int) {
        SearchCategory.values().forEach { category ->
            if (resourceID == category.resourceID) {
                data.category = category
            }
        }

        state.value = SearchScreenUIState.DataChanged
    }

    override fun updatePriceFieldValue(value: SearchPrice, isChecked: Boolean) {
        data.prices[value] = isChecked
        state.value = SearchScreenUIState.DataChanged
    }

    private fun processResponse(response: Response<YelpResponse>, offset: Int) {
        if (response.isSuccessful && response.body() != null) {
            val businessesFromResponse = response.body()!!.businesses

            if (businessesFromResponse.isEmpty() && offset == 0) {
                placeholderData.setEmptyBusinessesPlaceholder()
                state.value = SearchScreenUIState.Success
            }

            else if (businessesFromResponse.isEmpty() && offset > 0) {
                state.value = SearchScreenUIState.PageEnd
            }

            else {
                businesses.addAll(businessesFromResponse)
                state.value = SearchScreenUIState.Success
            }

        } else {
            placeholderData.setHTTPErrorPlaceholder(response.code())
            state.value = SearchScreenUIState.Error

            println("YELP RESPONSE STATUS CODE: ${response.code()}")
        }
    }
}