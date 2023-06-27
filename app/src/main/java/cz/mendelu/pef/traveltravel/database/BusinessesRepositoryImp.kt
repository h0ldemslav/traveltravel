package cz.mendelu.pef.traveltravel.database

import cz.mendelu.pef.traveltravel.model.BusinessData
import cz.mendelu.pef.traveltravel.model.BusinessEntity
import cz.mendelu.pef.traveltravel.model.StatisticsCategory
import cz.mendelu.pef.traveltravel.model.StatisticsCity
import kotlinx.coroutines.flow.Flow

class BusinessesRepositoryImp(private val businessesDao: BusinessesDao)
    : BusinessesRepository {
    override fun getAllBusinesses(): Flow<List<BusinessEntity>> {
        return businessesDao.getAllBusinesses()
    }

    override fun getAllBusinessesByCity(city: String): Flow<List<BusinessEntity>> {
        return businessesDao.getAllBusinessesByCity(city)
    }

    override fun getCitiesWithNumberOfVisitedPlaces(): Flow<List<StatisticsCity>> {
        return businessesDao.getCitiesWithNumberOfVisitedPlaces()
    }

    override fun getCategoriesWithNumberOfVisitedPlaces(): Flow<List<StatisticsCategory>> {
        return businessesDao.getCategoriesWithNumberOfVisitedPlaces()
    }

    override suspend fun getBusinessByID(id: Long): BusinessEntity {
        return businessesDao.getBusinessByID(id)
    }

    override suspend fun insertNewBusiness(business: BusinessEntity) {
        businessesDao.insertNewBusiness(business)
    }

    override suspend fun updateBusiness(business: BusinessEntity) {
        businessesDao.updateBusiness(business)
    }

    override suspend fun updateBusinessIsVisited(id: Long, isVisited: Boolean) {
        businessesDao.updateBusinessIsVisited(id, isVisited)
    }

    override suspend fun updateNote(id: Long, note: String?) {
        businessesDao.updateNote(id, note)
    }

    override suspend fun deleteBusinessByID(id: Long) {
        businessesDao.deleteBusinessByID(id)
    }

    override suspend fun deleteAllByCity(city: String) {
        businessesDao.deleteAllByCity(city)
    }

    override fun getAllCitiesFromBusinesses(businesses: List<BusinessData>): List<String> {
        val mutableList = mutableListOf<String>()

        businesses.forEach { business ->
            if (business.city !in mutableList) mutableList.add(business.city)
        }

        return mutableList.toList()
    }

    override fun convertBusinessEntityToData(businessEntity: BusinessEntity): BusinessData {
        return BusinessData(
            id = businessEntity.id,
            remoteId = businessEntity.remoteId,
            name = businessEntity.name,
            category = businessEntity.category,
            address = businessEntity.address,
            city = businessEntity.city,
            url = businessEntity.url,
            rating = businessEntity.rating,
            reviewCount = businessEntity.reviewCount,
            price = businessEntity.price,
            latitude = businessEntity.latitude,
            longitude = businessEntity.longitude,
            phone = businessEntity.phone,
            whenAdded = businessEntity.whenAdded,
            userNote = businessEntity.userNote,
            is_visited = businessEntity.is_visited
        )
    }

    override fun convertDataToBusinessEntity(businessData: BusinessData): BusinessEntity {
        return BusinessEntity(
            id = businessData.id,
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
            whenAdded = businessData.whenAdded,
            userNote = businessData.userNote,
            is_visited = businessData.is_visited
        )
    }


}