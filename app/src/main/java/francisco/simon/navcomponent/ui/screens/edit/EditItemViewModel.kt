package francisco.simon.navcomponent.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import francisco.simon.navcomponent.model.ItemsRepository
import francisco.simon.navcomponent.model.LoadResult
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

    private val _stateFlow = MutableStateFlow<LoadResult<ScreenState>>(LoadResult.Loading)

    val stateFlow: StateFlow<LoadResult<ScreenState>> = _stateFlow

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    init {
        viewModelScope.launch {
            val loadedItem = itemsRepository.getByIndex(index)
            _stateFlow.update {
                LoadResult.Success(ScreenState(loadedItem))
            }
        }
    }

    fun update(newValue: String) {
        val loadResult = _stateFlow.value
        if (loadResult !is LoadResult.Success) {
            return
        }
        viewModelScope.launch {
            showProgress(loadResult)
            itemsRepository.update(index, newValue)
            goBack()
        }
    }
    private fun showProgress(loadResult: LoadResult.Success<ScreenState>){
        val currentScreenState = loadResult.data
        val updatedScreenState = currentScreenState.copy(isEditInProgress = true)
        _stateFlow.update {
            LoadResult.Success(updatedScreenState)
        }
    }
    private suspend fun goBack(){
        _exitChannel.send(Unit)
    }


    data class ScreenState(
        val loadedItem: String,
        val isEditInProgress: Boolean = false
    )


    @AssistedFactory
    interface Factory {
        fun create(index: Int): EditItemViewModel
    }


}