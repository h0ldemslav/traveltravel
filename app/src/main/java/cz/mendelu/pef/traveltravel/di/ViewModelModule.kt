package cz.mendelu.pef.traveltravel.di

import cz.mendelu.pef.traveltravel.ui.activities.MainActivityViewModel
import cz.mendelu.pef.traveltravel.ui.screens.placedetail.PlaceDetailScreenViewModel
import cz.mendelu.pef.traveltravel.ui.screens.search.SearchScreenViewModel
import cz.mendelu.pef.traveltravel.ui.screens.settings.SettingsScreenViewModel
import cz.mendelu.pef.traveltravel.ui.screens.statistics.StatisticsScreenViewModel
import cz.mendelu.pef.traveltravel.ui.screens.wanttovisit.AddEditNoteScreenViewModel
import cz.mendelu.pef.traveltravel.ui.screens.wanttovisit.WantToVisitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchScreenViewModel(get(), get())
    }

    viewModel {
        PlaceDetailScreenViewModel(get(), get(), get())
    }

    viewModel {
        WantToVisitViewModel(get())
    }

    viewModel {
        AddEditNoteScreenViewModel(get())
    }

    viewModel {
        StatisticsScreenViewModel(get())
    }

    viewModel {
        SettingsScreenViewModel(get())
    }

    viewModel {
        MainActivityViewModel(get())
    }
}