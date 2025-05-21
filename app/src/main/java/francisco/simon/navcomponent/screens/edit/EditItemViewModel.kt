package francisco.simon.navcomponent.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import francisco.simon.navcomponent.model.ItemsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditItemViewModel.Factory::class)
class EditItemViewModel @AssistedInject constructor(
    @Assisted private val index: Int,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ScreenState>(ScreenState.Loading)

    val stateFlow: StateFlow<ScreenState> = _stateFlow

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    init {
        viewModelScope.launch {
            val loadedItem = itemsRepository.getByIndex(index)
            _stateFlow.update {
                ScreenState.Success(loadedItem)
            }
        }
    }

    fun update(newValue: String) {
        val currentState = _stateFlow.value
        if (currentState !is ScreenState.Success) {
            return
        }
        viewModelScope.launch {
            _stateFlow.update {
                currentState.copy(isEditInProgress = true)
            }
            itemsRepository.update(index, newValue)
            _exitChannel.send(Unit)
        }
    }


    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(
            val loadedItem: String,
            val isEditInProgress: Boolean = false
        ) : ScreenState()
    }

    @AssistedFactory
    interface Factory {
        fun create(index: Int): EditItemViewModel
    }


}