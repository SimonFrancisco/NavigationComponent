package francisco.simon.navcomponent.ui.screens.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import francisco.simon.navcomponent.model.LoadResult
import francisco.simon.navcomponent.model.tryUpdate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActionViewModel<State, Action>(
    private val delegate: Delegate<State, Action>
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<LoadResult<State>>(LoadResult.Loading)

    val stateFlow: StateFlow<LoadResult<State>> = _stateFlow

//    private val _exitChannel = Channel<Unit>()
//    val exitChannel: ReceiveChannel<Unit> = _exitChannel
//
//    private val _errorChannel = Channel<Exception>()
//    val errorChannel: ReceiveChannel<Exception> = _errorChannel

    private val _screenStateFlow = MutableStateFlow(ScreenState())
    val screenStateFlow: StateFlow<ScreenState> = _screenStateFlow

    init {
        load()
    }

    fun execute(action: Action) {
        viewModelScope.launch {
            showProgress()
            try {
                delegate.execute(action)
                goBack()
            } catch (e: Exception) {
                hideProgress()
                //_errorChannel.send(e)
                _screenStateFlow.update { oldState ->
                    oldState.copy(error = e)
                }
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            _stateFlow.value = LoadResult.Loading
            _stateFlow.value = try {
                LoadResult.Success(delegate.loadState())
            } catch (e: Exception) {
                LoadResult.Error(e)
            }

        }
    }

    fun onExitHandled() {
        _screenStateFlow.update { oldState ->
            oldState.copy(exit = null)
        }
    }

    fun onErrorHandled() {
        _screenStateFlow.update { oldState ->
            oldState.copy(error = null)
        }
    }

    private fun hideProgress() {
        _stateFlow.tryUpdate(delegate::hideProgress)
    }

    private fun showProgress() {
        _stateFlow.tryUpdate(delegate::showProgress)
    }

    private suspend fun goBack() {
        _screenStateFlow.update { oldState ->
            oldState.copy(exit = Unit)
        }
        //_exitChannel.send(Unit)
    }

    interface Delegate<State, Action> {
        suspend fun loadState(): State
        fun showProgress(input: State): State
        fun hideProgress(input: State): State
        suspend fun execute(action: Action)
    }

    data class ScreenState(
        val exit: Unit? = null,
        val error: Exception? = null
    )


}