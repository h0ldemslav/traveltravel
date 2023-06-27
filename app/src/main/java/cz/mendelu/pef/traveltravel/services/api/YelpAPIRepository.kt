package cz.mendelu.pef.traveltravel.services.api

import cz.mendelu.pef.traveltravel.model.Business
import cz.mendelu.pef.traveltravel.model.YelpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val RESULTS_LIMIT = 50
const val INITIAL_OFFSET = 0
const val MAX_OFFSET = 1000

// API supported locales: https://docs.developer.yelp.com/docs/resources-supported-locales
const val YELP_ENGLISH_LOCALE = "en_US"
const val YELP_CZECH_LOCALE = "cs_CZ"

interface YelpAPIRepository {
    @GET("businesses/search")
    suspend fun getBusinesses(
        @Query("location") locationName: String,
        @Query("categories") category: String,
        @Query("price") prices: List<Int>,
        @Query("limit") resultsLimit: Int = RESULTS_LIMIT,
        @Query("offset") offset: Int,
        @Query("locale") locale: String
    ): Response<YelpResponse>?

    @GET("businesses/{id}")
    suspend fun getBusinessByID(
        @Path("id") id: String,
        @Query("locale") locale: String
    ): Response<Business>?
}