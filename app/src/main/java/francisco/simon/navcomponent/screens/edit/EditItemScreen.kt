package francisco.simon.navcomponent.screens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import francisco.simon.navcomponent.R
import francisco.simon.navcomponent.screens.EditItemRoute
import francisco.simon.navcomponent.screens.EventConsumer
import francisco.simon.navcomponent.screens.LocalNavController
import francisco.simon.navcomponent.screens.edit.EditItemViewModel.ScreenState
import francisco.simon.navcomponent.screens.routeClass


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

    val screenState by viewModel.stateFlow.collectAsState()
    EditItemContent(
        state = screenState,
        onEditButtonClicked = viewModel::update
    )
}

@Composable
fun EditItemContent(
    state: ScreenState,
    onEditButtonClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            ScreenState.Loading -> CircularProgressIndicator()
            is ScreenState.Success -> SuccessEditItemContent(state, onEditButtonClicked)
        }
    }
}

@Composable
private fun SuccessEditItemContent(
    state: ScreenState.Success,
    onEditButtonClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var inputText by rememberSaveable {
            mutableStateOf(state.loadedItem)
        }
        OutlinedTextField(
            value = inputText,
            onValueChange = { text ->
                inputText = text
            },
            placeholder = {
                Text(stringResource(R.string.edit_item_title))
            },
            enabled = !state.isEditInProgress
        )
        Button(
            onClick = { onEditButtonClicked(inputText) },
            enabled = inputText.isNotBlank() && !state.isEditInProgress
        ) {
            Text(
                text = stringResource(R.string.edit),
            )
        }
        Box(
            modifier = Modifier.size(32.dp)
        ) {
            if (state.isEditInProgress) {
                CircularProgressIndicator(Modifier.fillMaxSize())
            }
        }

    }
}