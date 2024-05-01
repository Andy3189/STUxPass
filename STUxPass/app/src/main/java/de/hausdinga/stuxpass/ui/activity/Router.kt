package de.hausdinga.stuxpass.ui.activity

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.hausdinga.stuxpass.ui.list.TicketListView
import de.hausdinga.stuxpass.ui.nav.Screen
import de.hausdinga.stuxpass.ui.pass.Pass
import de.hausdinga.stuxpass.ui.theme.STUxPassTheme
import de.hausdinga.stuxpass.util.Constants

@Composable
fun Router() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.List.route) {
        composable(Screen.List.route) {
            TicketListView(
                navigateToTicket = { ticketID ->
                    navController.navigate(Screen.Ticket.createRoute(ticketID))
                }
            )
        }
        composable(Screen.Ticket.route) { backStackEntry ->
            Pass(
                ticketID = backStackEntry.arguments?.getString(Constants.ARG_TICKET_ID) ?: "",
                navigateBack = { navController.navigate(Screen.List.route) }
            )
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun RouterPreview() {
    STUxPassTheme {
        Router()
    }
}