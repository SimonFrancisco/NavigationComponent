package francisco.simon.navcomponent.ui.screens.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import francisco.simon.navcomponent.model.LoadResult
import francisco.simon.navcomponent.model.tryUpdate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActionViewModel<State, Action>(
    private val delegate: Delegate<State, Action>
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<LoadResult<State>>(LoadResult.Loading)

    val stateFlow: StateFlow<LoadResult<State>> = _stateFlow

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    init {
        viewModelScope.launch {
            val loadedState = delegate.loadState()
            _stateFlow.value = LoadResult.Success(loadedState)

        }
    }

    fun execute(action: Action) {
        viewModelScope.launch {
            showProgress()
            delegate.execute(action)
            goBack()
        }
    }

    private fun showProgress() {
        _stateFlow.tryUpdate(delegate::showProgress)
    }

    private suspend fun goBack() {
        _exitChannel.send(Unit)
    }

    interface Delegate<State, Action> {
        suspend fun loadState(): State
        fun showProgress(input: State): State
        suspend fun execute(action: Action)
    }
}