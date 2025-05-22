package francisco.simon.navcomponent.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import francisco.simon.navcomponent.R
import francisco.simon.navcomponent.ui.components.ItemDetails
import francisco.simon.navcomponent.ui.components.ItemDetailsState
import francisco.simon.navcomponent.ui.screens.action.ActionScreen
import francisco.simon.navcomponent.ui.screens.edit.EditItemViewModel.ScreenState


@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    ActionScreen(
        delegate = viewModel,
        content = { (screenState, onExecuteAction) ->
            EditItemContent(screenState, onExecuteAction)
        }
    )
}


@Composable
private fun EditItemContent(
    state: ScreenState,
    onEditButtonClicked: (String) -> Unit
) {
    ItemDetails(
        state = ItemDetailsState(
            loadedItem = state.loadedItem,
            textFieldPlaceHolder = stringResource(R.string.edit_item_title),
            actionButtonText = stringResource(R.string.edit),
            isActionInProgress = state.isEditInProgress
        ),
        onAddButtonClicked = onEditButtonClicked,
        modifier = Modifier.fillMaxSize()
    )

}