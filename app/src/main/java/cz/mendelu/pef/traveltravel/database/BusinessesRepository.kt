package cz.mendelu.pef.traveltravel.database

import cz.mendelu.pef.traveltravel.model.BusinessData
import cz.mendelu.pef.traveltravel.model.BusinessEntity
import cz.mendelu.pef.traveltravel.model.StatisticsCategory
import cz.mendelu.pef.traveltravel.model.StatisticsCity
import kotlinx.coroutines.flow.Flow

interface BusinessesRepository {
    fun getAllBusinesses(): Flow<List<BusinessEntity>>
    fun getAllBusinessesByCity(city: String): Flow<List<BusinessEntity>>
    fun getCitiesWithNumberOfVisitedPlaces(): Flow<List<StatisticsCity>>
    fun getCategoriesWithNumberOfVisitedPlaces(): Flow<List<StatisticsCategory>>
    suspend fun getBusinessByID(id: Long): BusinessEntity
    suspend fun insertNewBusiness(business: BusinessEntity)
    suspend fun updateBusiness(business: BusinessEntity)
    suspend fun updateBusinessIsVisited(id: Long, isVisited: Boolean)
    suspend fun updateNote(id: Long, note: String?)
    suspend fun deleteBusinessByID(id: Long)
    suspend fun deleteAllByCity(city: String)

    fun getAllCitiesFromBusinesses(businesses: List<BusinessData>): List<String>
    fun convertBusinessEntityToData(businessEntity: BusinessEntity): BusinessData
    fun convertDataToBusinessEntity(businessData: BusinessData): BusinessEntity
}