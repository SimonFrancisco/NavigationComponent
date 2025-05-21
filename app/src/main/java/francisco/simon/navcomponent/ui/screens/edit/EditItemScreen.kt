package francisco.simon.navcomponent.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import francisco.simon.navcomponent.R
import francisco.simon.navcomponent.model.LoadResult
import francisco.simon.navcomponent.model.LoadResultContent
import francisco.simon.navcomponent.ui.components.ItemDetails
import francisco.simon.navcomponent.ui.components.ItemDetailsState
import francisco.simon.navcomponent.ui.screens.EditItemRoute
import francisco.simon.navcomponent.ui.screens.EventConsumer
import francisco.simon.navcomponent.ui.screens.LocalNavController
import francisco.simon.navcomponent.ui.screens.edit.EditItemViewModel.ScreenState
import francisco.simon.navcomponent.ui.screens.routeClass


@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    val navController = LocalNavController.current
    EventConsumer(channel = viewModel.exitChannel) {
        if (navController.currentBackStackEntry.routeClass() == EditItemRoute::class) {
            navController.popBackStack()
        }
    }

    val loadResult by viewModel.stateFlow.collectAsState()
    EditItemContent(
        loadResult = loadResult,
        onEditButtonClicked = viewModel::update
    )
}

@Composable
fun EditItemContent(
    loadResult: LoadResult<ScreenState>,
    onEditButtonClicked: (String) -> Unit
) {
    LoadResultContent(
        loadResult = loadResult,
        content = { screenState->
            SuccessEditItemContent(
                state = screenState,
                onEditButtonClicked
            )
        }
    )

}

@Composable
private fun SuccessEditItemContent(
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