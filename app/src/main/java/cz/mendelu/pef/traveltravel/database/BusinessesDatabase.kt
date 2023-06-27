package cz.mendelu.pef.traveltravel.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.pef.traveltravel.model.BusinessEntity

@Database(
    entities = [BusinessEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BusinessesDatabase : RoomDatabase() {

    abstract fun businessesDao(): BusinessesDao

    companion object {

        private var INSTANCE: BusinessesDatabase? = null

        fun getDatabase(context: Context): BusinessesDatabase {
            if (INSTANCE == null) {
                synchronized(BusinessesDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BusinessesDatabase::class.java, "businesses_database"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}
