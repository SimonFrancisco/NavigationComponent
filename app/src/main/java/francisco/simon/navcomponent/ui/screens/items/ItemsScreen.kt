package francisco.simon.navcomponent.ui.screens.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import francisco.simon.navcomponent.model.LoadResult
import francisco.simon.navcomponent.ui.components.LoadResultContent
import francisco.simon.navcomponent.ui.screens.ItemGraph.EditItemRoute
import francisco.simon.navcomponent.ui.screens.LocalNavController
import francisco.simon.navcomponent.ui.screens.items.ItemsViewModel.ScreenState

@Composable
fun ItemsScreen() {
    val viewModel: ItemsViewModel = hiltViewModel()
    val screenState = viewModel.stateFlow.collectAsState()
    val navController = LocalNavController.current
    ItemsContent(
        getLoadResult = {
            screenState.value
        },
        onItemClicked = { index ->
            navController.navigate(EditItemRoute(index))
        }
    )
}

@Composable
fun ItemsContent(
    getLoadResult: () -> LoadResult<ScreenState>,
    onItemClicked: (Int) -> Unit
) {
    LoadResultContent(
        loadResult = getLoadResult(),
        content = { screenState ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(screenState.items) { index, item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .clickable {
                                onItemClicked(index)
                            }
                            .fillMaxWidth()
                            .padding(12.dp)
                    )

                }
            }
        }
    )

}