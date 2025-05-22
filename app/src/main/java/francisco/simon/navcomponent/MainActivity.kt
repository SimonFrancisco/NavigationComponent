package francisco.simon.navcomponent

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import francisco.simon.navcomponent.ui.screens.AppNavigationBar
import francisco.simon.navcomponent.ui.screens.AppToolbar
import francisco.simon.navcomponent.ui.screens.ItemGraph
import francisco.simon.navcomponent.ui.screens.ItemGraph.AddItemRoute
import francisco.simon.navcomponent.ui.screens.ItemGraph.EditItemRoute
import francisco.simon.navcomponent.ui.screens.ItemGraph.ItemsRoute
import francisco.simon.navcomponent.ui.screens.LocalNavController
import francisco.simon.navcomponent.ui.screens.MainTabs
import francisco.simon.navcomponent.ui.screens.NavigationUpAction
import francisco.simon.navcomponent.ui.screens.ProfileGraph
import francisco.simon.navcomponent.ui.screens.ProfileGraph.ProfileRoute
import francisco.simon.navcomponent.ui.screens.SettingsGraph
import francisco.simon.navcomponent.ui.screens.SettingsGraph.SettingsRoute
import francisco.simon.navcomponent.ui.screens.add.AddItemScreen
import francisco.simon.navcomponent.ui.screens.edit.EditItemScreen
import francisco.simon.navcomponent.ui.screens.items.ItemsScreen
import francisco.simon.navcomponent.ui.screens.profile.ProfileScreen
import francisco.simon.navcomponent.ui.screens.routeClass
import francisco.simon.navcomponent.ui.screens.settings.SettingsScreen
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
        SettingsRoute::class -> R.string.settings_screen
        ProfileRoute::class -> R.string.profile_screen
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

        },
        bottomBar = {
            AppNavigationBar(navController = navController, tabs = MainTabs)
        }
    ) { paddingValues ->
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            val intentHost = (LocalActivity.current as Activity).intent?.data?.host
            val startDestination: Any = when (intentHost) {
                "settings" -> SettingsGraph
                "items" -> ItemGraph
                else -> ProfileGraph
            }
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                navigation<ItemGraph>(startDestination = ItemsRoute) {
                    composable<ItemsRoute> {
                        ItemsScreen()
                    }
                    composable<AddItemRoute> { AddItemScreen() }
                    composable<EditItemRoute>(
                        deepLinks = listOf(EditItemRoute.Link)
                    ) { entry ->
                        val route: EditItemRoute = entry.toRoute()
                        EditItemScreen(index = route.index)
                    }
                }
                navigation<ProfileGraph>(startDestination = ProfileRoute) {
                    composable<ProfileRoute> {
                        ProfileScreen()
                    }
                }
                navigation<SettingsGraph>(
                    startDestination = SettingsRoute,
                    deepLinks = listOf(SettingsGraph.Link)
                ) {
                    composable<SettingsRoute> {
                        SettingsScreen()
                    }
                }
            }
        }
    }


}
