package cz.mendelu.pef.traveltravel.di

import cz.mendelu.pef.traveltravel.TravelTravelApp
import cz.mendelu.pef.traveltravel.database.BusinessesDatabase
import org.koin.dsl.module

val databaseModule = module {
    fun getDatabase(): BusinessesDatabase {
        return BusinessesDatabase.getDatabase(TravelTravelApp.appContext)
    }

    single {
        getDatabase()
    }
}