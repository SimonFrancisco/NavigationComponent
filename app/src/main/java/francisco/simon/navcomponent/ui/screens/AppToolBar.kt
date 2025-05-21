package francisco.simon.navcomponent.ui.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class NavigationUpAction {
    data object Hidden : NavigationUpAction()
    data class Visible(
        val onClick: () -> Unit,
    ) : NavigationUpAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    @StringRes titleRes: Int,
    navigateUpAction: NavigationUpAction
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(titleRes)
            )
        },
        navigationIcon = {
            if (navigateUpAction is NavigationUpAction.Visible) {
                IconButton(
                    onClick = navigateUpAction.onClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )

}
