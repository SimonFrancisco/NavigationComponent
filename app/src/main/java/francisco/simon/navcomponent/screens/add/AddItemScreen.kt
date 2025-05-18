package francisco.simon.navcomponent.screens.add

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
import francisco.simon.navcomponent.screens.AddItemRoute
import francisco.simon.navcomponent.screens.EventConsumer
import francisco.simon.navcomponent.screens.LocalNavController
import francisco.simon.navcomponent.screens.add.AddItemViewModel.ScreenState

@Composable
fun AddItemScreen() {
    val viewModel: AddItemViewModel = hiltViewModel()
    val screenState by viewModel.stateFlow.collectAsState()
    AddItemContent(
        screenState = screenState,
        onAddButtonClicked = viewModel::add
    )
    val navController = LocalNavController.current
    EventConsumer(viewModel.exitChannel) {
        if (navController.currentBackStackEntry?.destination?.route == AddItemRoute) {
            navController.popBackStack()
        }
    }
}

@Composable
fun AddItemContent(
    modifier: Modifier = Modifier,
    screenState: ScreenState,
    onAddButtonClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var inputText by rememberSaveable {
            mutableStateOf("")
        }
        OutlinedTextField(
            value = inputText,
            onValueChange = { text ->
                inputText = text
            },
            placeholder = {
                Text(stringResource(R.string.enter_new_item))
            },
            enabled = screenState.isTextInputEnabled,
        )
        Button(
            onClick = { onAddButtonClicked(inputText) },
            enabled = screenState.isAddButtonEnabled(inputText)
        ) {
            Text(
                text = stringResource(R.string.add),

                )
        }
        Box(
            modifier = Modifier.size(32.dp)
        ) {
            if (screenState.isProgressVisible) {
                CircularProgressIndicator(Modifier.fillMaxSize())
            }
        }

    }
}