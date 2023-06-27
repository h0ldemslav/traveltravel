package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

import androidx.compose.runtime.*
import cz.mendelu.pef.traveltravel.architecture.BaseViewModel
import cz.mendelu.pef.traveltravel.database.BusinessesRepository
import kotlinx.coroutines.launch

class AddEditNoteScreenViewModel(private val businessesRepository: BusinessesRepository)
    : BaseViewModel(), AddEditNoteScreenActions {

    val state: MutableState<AddEditNoteScreenUIState> =
        mutableStateOf(AddEditNoteScreenUIState.Default)

    var note by mutableStateOf<String?>(null)

    fun getBusinessData(id: Long) {
        launch {
            val convertedToBusinessData = businessesRepository.convertBusinessEntityToData(
                businessesRepository.getBusinessByID(id)
            )

            note = convertedToBusinessData.userNote
            state.value = AddEditNoteScreenUIState.Success
        }
    }

    override fun updateNote(id: Long, note: String?) {
        launch {
            businessesRepository.updateNote(id, note)
            state.value = AddEditNoteScreenUIState.Updated
        }
    }
}