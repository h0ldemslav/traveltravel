package cz.mendelu.pef.traveltravel.ui.screens.placedetail

import androidx.compose.runtime.*
import com.google.gson.Gson
import cz.mendelu.pef.traveltravel.architecture.BaseViewModel
import cz.mendelu.pef.traveltravel.database.BusinessesRepository
import cz.mendelu.pef.traveltravel.localstore.LocalStoreManager
import cz.mendelu.pef.traveltravel.model.Business
import cz.mendelu.pef.traveltravel.model.BusinessData
import cz.mendelu.pef.traveltravel.model.BusinessEntity
import cz.mendelu.pef.traveltravel.navigation.otherCity
import cz.mendelu.pef.traveltravel.services.api.YelpAPIRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import cz.mendelu.pef.traveltravel.R

class PlaceDetailScreenViewModel(
    private val localStoreManager: LocalStoreManager,
    private val businessesRepository: BusinessesRepository,
    private val apiRepository: YelpAPIRepository
) : BaseViewModel(), PlaceDetailScreenActions {

    val state: MutableState<PlaceDetailScreenUIState> =
        mutableStateOf(PlaceDetailScreenUIState.Loading)

    fun getBusinessRecordFromStore() {
        launch {
            localStoreManager.getBusinessRecordFromStore().collect {
                if (it.isNotEmpty()) {
                    val gson = Gson()
                    val business = gson.fromJson(it, BusinessData::class.java)
                    state.value = PlaceDetailScreenUIState.Success(business)
                } else {
                    state.value = PlaceDetailScreenUIState.Error(R.string.failed_to_load_data_from_datastore)
                }
            }
        }
    }

    fun getBusinessByIDFromDatabase(id: Long) {
        launch {
            val business = businessesRepository.convertBusinessEntityToData(
                businessesRepository.getBusinessByID(id)
            )

            state.value = PlaceDetailScreenUIState.Success(business)
        }
    }

    fun updateBusinessData(businessData: BusinessData, locale: String) {
        launch {
            val response = apiRepository.getBusinessByID(businessData.remoteId, locale = locale)

            if (response != null) {
                processResponse(response = response, oldBusinessData = businessData)
            } else {
                state.value = PlaceDetailScreenUIState.Error(R.string.failed_to_refresh_no_internet)
            }
        }
    }

    override fun addBusinessToWantToVisit(businessData: BusinessData) {
        val businessEntity = BusinessEntity(
            remoteId = businessData.remoteId,
            name = businessData.name,
            category = businessData.category,
            address = businessData.address,
            city = businessData.city,
            url = businessData.url,
            rating = businessData.rating,
            reviewCount = businessData.reviewCount,
            price = businessData.price,
            latitude = businessData.latitude,
            longitude = businessData.longitude,
            phone = businessData.phone,
            whenAdded = System.currentTimeMillis() // must be in order to refresh data from the api
        )

        launch {
            businessesRepository.insertNewBusiness(businessEntity)
        }
    }

    private fun processResponse(response: Response<Business>, oldBusinessData: BusinessData) {
        if (response.isSuccessful && response.body() != null) {
            val updatedBusiness = response.body()
            var address = ""

            updatedBusiness?.location?.let {
                it.display_address.forEachIndexed { index, addressPart ->
                    address += addressPart
                    if (index != updatedBusiness.location.display_address.lastIndex) address += ", "
                }
            }

            val convertedBusinessData = BusinessData(
                id = oldBusinessData.id,
                remoteId = updatedBusiness!!.id,
                name = updatedBusiness.name,
                category = oldBusinessData.category, // remains the same
                address = address,
                city = updatedBusiness.location.city ?: otherCity,
                url = updatedBusiness.url,
                rating = updatedBusiness.rating,
                reviewCount = updatedBusiness.review_count,
                price = updatedBusiness.price,
                latitude = updatedBusiness.coordinates.latitude.toDouble(),
                longitude = updatedBusiness.coordinates.longitude.toDouble(),
                phone = updatedBusiness.display_phone,
                whenAdded = System.currentTimeMillis(), // must be set, in order to future updates
                is_visited = oldBusinessData.is_visited, // remains the same
                userNote = oldBusinessData.userNote // remains the same
            )

            updateBusinessInDatabase(convertedBusinessData)

            state.value = PlaceDetailScreenUIState.Success(
                business = convertedBusinessData,
                snackbarMessageID = R.string.refresh_data_success
            )
        } else {
            state.value = PlaceDetailScreenUIState.Error(R.string.refresh_data_failed)
        }
    }

    private fun updateBusinessInDatabase(businessData: BusinessData) {
        val business = businessesRepository.convertDataToBusinessEntity(businessData)

        launch {
            businessesRepository.updateBusiness(business)
        }
    }
}