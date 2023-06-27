package cz.mendelu.pef.traveltravel.services.api

import cz.mendelu.pef.traveltravel.model.Business
import cz.mendelu.pef.traveltravel.model.YelpResponse
import retrofit2.Response
import java.io.IOException

class YelpAPIRepositoryImp : YelpAPIRepository {
    override suspend fun getBusinesses(
        locationName: String,
        category: String,
        prices: List<Int>,
        resultsLimit: Int,
        offset: Int,
        locale: String
    ): Response<YelpResponse>? {
        return try {
            RetrofitInstance.api.getBusinesses(
                locationName = locationName,
                category = category,
                prices = prices,
                offset = offset,
                locale = locale
            )
        } catch (e: IOException) {
            null
        }
    }

    override suspend fun getBusinessByID(id: String, locale: String): Response<Business>? {
        return try {
            RetrofitInstance.api.getBusinessByID(id = id, locale = locale)
        } catch (e: IOException) {
            null
        }
    }
}