package francisco.simon.navcomponent.model

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> LoadResultContent(
    loadResult: LoadResult<T>,
    content: @Composable (T) -> Unit,
    modifier: Modifier = Modifier
) {
    when (loadResult) {
        LoadResult.Loading -> Box(modifier = modifier) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is LoadResult.Success -> content(loadResult.data)
    }
}