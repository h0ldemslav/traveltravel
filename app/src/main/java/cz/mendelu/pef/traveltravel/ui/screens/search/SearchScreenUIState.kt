package cz.mendelu.pef.traveltravel.ui.screens.search

sealed class SearchScreenUIState {
    object Default : SearchScreenUIState()
    object DataChanged : SearchScreenUIState()
    object InitialLoading : SearchScreenUIState()
    object Success : SearchScreenUIState()
    class Paginating(val offset: Int) : SearchScreenUIState()
    object PageEnd : SearchScreenUIState()
    object Error : SearchScreenUIState()
}