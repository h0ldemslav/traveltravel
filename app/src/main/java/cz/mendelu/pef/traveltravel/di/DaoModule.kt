package cz.mendelu.pef.traveltravel.di

import cz.mendelu.pef.traveltravel.database.BusinessesDao
import cz.mendelu.pef.traveltravel.database.BusinessesDatabase
import org.koin.dsl.module

val daoModule = module {
    fun getBusinessesDao(database: BusinessesDatabase): BusinessesDao {
        return database.businessesDao()
    }

    single {
        getBusinessesDao(get())
    }
}