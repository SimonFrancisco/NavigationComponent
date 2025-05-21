package francisco.simon.navcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import francisco.simon.navcomponent.screens.AddItemRoute
import francisco.simon.navcomponent.screens.AppToolbar
import francisco.simon.navcomponent.screens.EditItemRoute
import francisco.simon.navcomponent.screens.ItemsRoute
import francisco.simon.navcomponent.screens.LocalNavController
import francisco.simon.navcomponent.screens.NavigationUpAction
import francisco.simon.navcomponent.screens.add.AddItemScreen
import francisco.simon.navcomponent.screens.edit.EditItemScreen
import francisco.simon.navcomponent.screens.items.ItemsScreen
import francisco.simon.navcomponent.screens.routeClass
import francisco.simon.navcomponent.ui.theme.NavigationComponentTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationComponentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavApp()
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val titleRes = when (currentBackStackEntry.routeClass()) {
        ItemsRoute::class -> R.string.items_screen_title
        AddItemRoute::class -> R.string.add_item_screen_title
        EditItemRoute::class -> R.string.edit_item_screen
        else -> R.string.app_name
    }
    Scaffold(
        topBar = {
            AppToolbar(
                titleRes = titleRes,
                navigateUpAction = if (navController.previousBackStackEntry == null) {
                    NavigationUpAction.Hidden
                } else {
                    NavigationUpAction.Visible(onClick = {
                        navController.navigateUp()
                    })
                }
            )
        },
        floatingActionButton = {
            if (currentBackStackEntry.routeClass() == ItemsRoute::class) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddItemRoute) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, contentDescription = null
                    )
                }
            }

        }
    ) { paddingValues ->
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = ItemsRoute,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                composable<ItemsRoute> {
                    ItemsScreen()
                }
                composable<AddItemRoute> { AddItemScreen() }

                composable<EditItemRoute> { entry ->
                    val route: EditItemRoute = entry.toRoute()
                    EditItemScreen(index = route.index)
                }
            }
        }
    }


}
