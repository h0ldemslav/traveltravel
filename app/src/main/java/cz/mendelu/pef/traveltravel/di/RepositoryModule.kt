package cz.mendelu.pef.traveltravel.di

import cz.mendelu.pef.traveltravel.database.BusinessesRepository
import cz.mendelu.pef.traveltravel.database.BusinessesRepositoryImp
import cz.mendelu.pef.traveltravel.services.api.YelpAPIRepository
import cz.mendelu.pef.traveltravel.services.api.YelpAPIRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
    single<YelpAPIRepository> {
        YelpAPIRepositoryImp()
    }

    single<BusinessesRepository> {
        BusinessesRepositoryImp(get())
    }
}