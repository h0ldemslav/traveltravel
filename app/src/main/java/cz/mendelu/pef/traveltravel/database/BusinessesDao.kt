package cz.mendelu.pef.traveltravel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.traveltravel.model.BusinessEntity
import cz.mendelu.pef.traveltravel.model.StatisticsCategory
import cz.mendelu.pef.traveltravel.model.StatisticsCity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessesDao {
    @Query("SELECT * FROM businesses")
    fun getAllBusinesses(): Flow<List<BusinessEntity>>

    @Query("SELECT * FROM businesses WHERE city = :city")
    fun getAllBusinessesByCity(city: String): Flow<List<BusinessEntity>>

    @Query("SELECT * FROM businesses WHERE id = :id")
    suspend fun getBusinessByID(id: Long): BusinessEntity

    @Query("" +
            "SELECT city, COUNT(is_visited) AS number_of_visited_places " +
            "FROM businesses " +
            "WHERE is_visited = 1 " +
            "GROUP BY city"
    )
    fun getCitiesWithNumberOfVisitedPlaces(): Flow<List<StatisticsCity>>

    @Query("" +
            "SELECT category AS name, COUNT(is_visited) AS number_of_visited_places " +
            "FROM businesses " +
            "WHERE is_visited = 1 " +
            "GROUP BY category"
    )
    fun getCategoriesWithNumberOfVisitedPlaces(): Flow<List<StatisticsCategory>>

    @Query("UPDATE businesses SET is_visited = :isVisited WHERE id = :id")
    suspend fun updateBusinessIsVisited(id: Long, isVisited: Boolean)

    @Query("UPDATE businesses SET user_note = :note WHERE id = :id")
    suspend fun updateNote(id: Long, note: String?)

    @Query("DELETE FROM businesses WHERE city = :city")
    suspend fun deleteAllByCity(city: String)

    @Query("DELETE FROM businesses WHERE id = :id")
    suspend fun deleteBusinessByID(id: Long)

    @Insert
    suspend fun insertNewBusiness(business: BusinessEntity)

    @Update
    suspend fun updateBusiness(business: BusinessEntity)
}