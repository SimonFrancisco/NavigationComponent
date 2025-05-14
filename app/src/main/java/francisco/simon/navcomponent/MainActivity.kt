package francisco.simon.navcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import francisco.simon.navcomponent.screens.AddItemRoute
import francisco.simon.navcomponent.screens.ItemsRoute
import francisco.simon.navcomponent.screens.LocalNavController
import francisco.simon.navcomponent.screens.add.AddItemScreen
import francisco.simon.navcomponent.screens.items.ItemsScreen
import francisco.simon.navcomponent.ui.theme.NavigationComponentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationComponentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavApp()
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = ItemsRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(ItemsRoute) {
                ItemsScreen()
            }
            composable(AddItemRoute) { AddItemScreen() }
        }
    }

}
