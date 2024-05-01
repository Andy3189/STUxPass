package de.hausdinga.stuxpass.repository.remote

object RideConstants {
    const val URL_BASE = "https://abo.ride-ticketing.de/v1/"
    const val URL_LOGIN = "campus/login"
    const val URL_BOOKING_ID = "auth/my-booking-id"
    const val URL_TICKET_SYNC = "campus/sync-semester-tickets"
    const val URL_PARTNER = "partner/{partnerID}"
    const val URL_TICKET_COLLECTION = "subscriptions/partner/{partnerID}"
    const val URL_TICKET_DATA = "ticket/collection/{partnerID}/{bookingID}"

    const val HEADER_AUTH = "Authorization"
    const val PARAM_PARTNER_ID = "partnerID"
    const val PARAM_BOOKING_ID = "bookingID"
}