package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter
import cz.mendelu.pef.traveltravel.ui.elements.BackArrowScreen
import cz.mendelu.pef.traveltravel.ui.elements.BasicTextArea
import cz.mendelu.pef.traveltravel.ui.elements.PlaceholderInfo
import org.koin.androidx.compose.getViewModel

@Composable
fun AddEditNoteScreen(
    navigation: NavigationRouter,
    viewModel: AddEditNoteScreenViewModel = getViewModel(),
    id: Long?
) {
    val maxNumOfCharacters = 256
    var errorID by remember { mutableStateOf<Int?>(null) }

    viewModel.state.value.let { state ->
        when (state) {
            AddEditNoteScreenUIState.Default -> {
                // ID must be always provided
                if (id != null) {
                    viewModel.getBusinessData(id)
                } else {
                    viewModel.state.value =
                        AddEditNoteScreenUIState.Error(R.drawable.undraw_no_data)
                }
            }

            AddEditNoteScreenUIState.Success -> {}

            AddEditNoteScreenUIState.Updated -> {
                LaunchedEffect(state) {
                    navigation.returnBack()
                    viewModel.state.value = AddEditNoteScreenUIState.Success
                }
            }

            is AddEditNoteScreenUIState.Error -> {
                errorID = state.messageID
            }
        }
    }

    BackArrowScreen(
        navigation = navigation,
        topBarTitle = stringResource(id = R.string.note_screen_title),
        topBarAction = { if (id != null) viewModel.updateNote(id, null) },
        topBarActionIcon = if (viewModel.note != null) Icons.Default.Delete else null,
        onArrowClick = { navigation.returnBack() }
    ) { padding ->
        AddEditNoteScreenContent(
            contentPadding = padding,
            id = id,
            note = viewModel.note ?: "",
            onNoteChange = {  if (it.length <= maxNumOfCharacters) viewModel.note = it },
            actions = viewModel,
            errorID = errorID
        )
    }
}

@Composable
fun AddEditNoteScreenContent(
    contentPadding: PaddingValues,
    id: Long?,
    note: String,
    onNoteChange: (String) -> Unit,
    actions: AddEditNoteScreenActions,
    errorID: Int?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .padding(top = 35.dp)
            .padding(horizontal = 16.dp)
    ) {
        if (errorID == null) {
            BasicTextArea(
                value = note,
                onValueChange = onNoteChange,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )

            OutlinedButton(
                modifier = Modifier.padding(top = 16.dp),
                onClick = { if (id != null) actions.updateNote(id, note) }
            ) {
                Text(text = stringResource(id = R.string.note_screen_button_label))
            }
        } else {
            PlaceholderInfo(
                vectorResourceID = errorID,
                textResourceID = R.string.database_operation_failed,
                supportingTextResourceID = R.string.database_operation_failed_supporting_text
            )
        }
    }
}