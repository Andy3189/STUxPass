package de.hausdinga.stuxpass.ui.nav

import de.hausdinga.stuxpass.util.Constants

sealed class Screen(val route: String) {
    object List : Screen(Constants.ROUTE_LIST)
    object Ticket : Screen(Constants.ROUTE_TICKET) {
        fun createRoute(ticketID: String) = "${Constants.ROUTE_TICKET_BASE}/$ticketID"
    }
}