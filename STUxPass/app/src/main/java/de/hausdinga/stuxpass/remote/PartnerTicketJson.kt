package de.hausdinga.stuxpass.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscriptionReference(
    val id: String,
    val bookingId: String,
    val orderId: String,
    val subscriptionId: String,
    val userId: String,
    val partnerId: String
)

@JsonClass(generateAdapter = true)
data class PartnerTicketJson(
    val id: String,
    val aboNumber: Int,
    val subscriptionReference: SubscriptionReference
)