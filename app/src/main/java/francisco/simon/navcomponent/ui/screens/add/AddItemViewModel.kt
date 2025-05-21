package francisco.simon.navcomponent.ui.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import francisco.simon.navcomponent.model.ItemsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ScreenState())
    val stateFlow: StateFlow<ScreenState> = _stateFlow

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    fun add(title: String) {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isAddInProgress = true) }
            itemsRepository.add(title)
            _exitChannel.send(Unit)
        }
    }

    data class ScreenState(
        private val isAddInProgress: Boolean = false
    ) {
        val isTextInputEnabled: Boolean get() = !isAddInProgress
        val isProgressVisible: Boolean get() = isAddInProgress
        fun isAddButtonEnabled(input: String): Boolean {
            return input.isNotBlank() && !isAddInProgress
        }
    }
}