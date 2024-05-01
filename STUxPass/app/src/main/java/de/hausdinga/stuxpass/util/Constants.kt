package de.hausdinga.stuxpass.util

object Constants {
    const val PREF_NAME = "preferences"
    const val ARG_TICKET_ID = "ticketID"
    const val TU_RIDE_PARTNER_ID = "5f9c379c567a6decadae13a5b6132ece"
    const val RIDE_REALM_ID = "ride"
    const val TICKET_STATE_ACTIVE = "activated"
    const val TICKET_VALID_WHERE = "bundesweit im ÖPNV"

    const val ROUTE_LIST = "list"
    const val ROUTE_TICKET_BASE = "ticket"
    const val ROUTE_TICKET = "$ROUTE_TICKET_BASE/{$ARG_TICKET_ID}"
}