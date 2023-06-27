package cz.mendelu.pef.traveltravel

import android.app.Application
import android.content.Context
import cz.mendelu.pef.traveltravel.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TravelTravelApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        startKoin {
            androidContext(this@TravelTravelApp)

            modules(
                listOf(
                    viewModelModule,
                    databaseModule,
                    daoModule,
                    repositoryModule,
                    managerModule
                )
            )
        }
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}