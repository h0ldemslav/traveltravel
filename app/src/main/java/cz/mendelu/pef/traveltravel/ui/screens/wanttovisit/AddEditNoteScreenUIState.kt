package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

sealed class AddEditNoteScreenUIState {
    object Default : AddEditNoteScreenUIState()
    object Success : AddEditNoteScreenUIState()
    object Updated : AddEditNoteScreenUIState()
    class Error(val messageID: Int) : AddEditNoteScreenUIState()
}
