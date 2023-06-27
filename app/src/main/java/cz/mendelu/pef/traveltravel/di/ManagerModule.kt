package cz.mendelu.pef.traveltravel.di

import cz.mendelu.pef.traveltravel.localstore.LocalStoreManager
import org.koin.dsl.module

val managerModule = module {
    single {
        LocalStoreManager(get())
    }
}