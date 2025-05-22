package francisco.simon.navcomponent.ui.screens.action

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import francisco.simon.navcomponent.ui.components.LoadResultContent
import francisco.simon.navcomponent.ui.EventConsumer
import francisco.simon.navcomponent.ui.components.ExceptionToMessageMapper
import francisco.simon.navcomponent.ui.screens.LocalNavController
import francisco.simon.navcomponent.ui.screens.action.ActionViewModel.Delegate
import francisco.simon.navcomponent.ui.screens.routeClass

data class ActionContentState<State, Action>(
    val state: State,
    val onExecuteAction: (Action) -> Unit
)

@Composable
fun <State, Action> ActionScreen(
    delegate: Delegate<State, Action>,
    content: @Composable (ActionContentState<State, Action>) -> Unit,
    modifier: Modifier = Modifier,
    exceptionToMessageMapper: ExceptionToMessageMapper = ExceptionToMessageMapper.Default
) {
    val viewModel = viewModel<ActionViewModel<State, Action>> {
        ActionViewModel(delegate)
    }
    val navController = LocalNavController.current
    val rememberedScreenRoute = remember {
        navController.currentBackStackEntry.routeClass()
    }
    EventConsumer(channel = viewModel.exitChannel) {
        if (rememberedScreenRoute == navController.currentBackStackEntry.routeClass()) {
            navController.popBackStack()
        }
    }
    val context = LocalContext.current
    EventConsumer(channel = viewModel.errorChannel) { exception ->
        val message = ExceptionToMessageMapper.Default.getUserMessage(exception,context)
        Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
    }
    val loadResult by viewModel.stateFlow.collectAsState()
    LoadResultContent(
        loadResult = loadResult,
        content = { state ->
            val actionContentState = ActionContentState(
                state = state,
                onExecuteAction = viewModel::execute
            )
            content(actionContentState)
        },
        modifier = modifier,
        onTryAgainAction = viewModel::load
    )
}