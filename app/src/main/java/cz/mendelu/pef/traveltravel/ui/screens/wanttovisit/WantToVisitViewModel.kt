package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.architecture.BaseViewModel
import cz.mendelu.pef.traveltravel.database.BusinessesRepository
import cz.mendelu.pef.traveltravel.model.BusinessData
import kotlinx.coroutines.launch

class WantToVisitViewModel(private val businessesRepository: BusinessesRepository)
    : BaseViewModel(), WantToVisitActions
{
    val state: MutableState<WantToVisitUIState> =
        mutableStateOf(WantToVisitUIState.Default)

    fun getAllBusinesses() {
        launch {
            businessesRepository.getAllBusinesses().collect { businesses ->
                val convertedBusinesses = businesses.map {
                    businessesRepository.convertBusinessEntityToData(it)
                }

                state.value = WantToVisitUIState.Success(convertedBusinesses)
            }
        }
    }

    fun getAllBusinessesByCity(city: String) {
        launch {
            businessesRepository.getAllBusinessesByCity(city).collect { businesses ->
                val convertedBusinesses = businesses.map {
                    businessesRepository.convertBusinessEntityToData(it)
                }

                state.value = WantToVisitUIState.Success(convertedBusinesses)
            }
        }
    }

    override fun updateBusinessIsVisited(business: BusinessData, isVisited: Boolean) {
        val id = business.id

        if (id != null) {
            launch {
                businessesRepository.updateBusinessIsVisited(id, isVisited)
            }
        } else {
            state.value = WantToVisitUIState.Error(R.string.database_operation_failed)
        }
    }

    override fun deleteAllBusinessByCity(city: String) {
        launch {
            businessesRepository.deleteAllByCity(city)
        }
    }

    override fun deleteBusiness(businessData: BusinessData) {
        val id = businessData.id

        if (id != null) {
            launch {
                businessesRepository.deleteBusinessByID(id)
            }
        } else {
            state.value = WantToVisitUIState.Error(R.string.database_operation_failed)
        }
    }

    fun getAllCitiesFromBusinesses(businesses: List<BusinessData>): List<String> {
        return businessesRepository.getAllCitiesFromBusinesses(businesses)
    }
}