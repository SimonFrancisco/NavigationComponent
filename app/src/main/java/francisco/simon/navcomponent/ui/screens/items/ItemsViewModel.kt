package francisco.simon.navcomponent.ui.screens.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import francisco.simon.navcomponent.model.ItemsRepository
import francisco.simon.navcomponent.model.LoadResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    itemsRepository: ItemsRepository
) : ViewModel() {

    val stateFlow: StateFlow<LoadResult<ScreenState>> = itemsRepository.getItems()
        .map{
            LoadResult.Success(ScreenState(it))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoadResult.Loading
        )

        data class ScreenState(val items: List<String>)

}