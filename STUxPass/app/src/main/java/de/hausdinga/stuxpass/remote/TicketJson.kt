package de.hausdinga.stuxpass.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TicketJson(
    val orderId: String,
    val validFrom: String,
    val preName: String,
    val surName: String,
    val subscriptionId: String,
    val validTo: String,
    val ticketName: String,
    val codeWord: String,
    val id: String,
    val barcode: String,
    val status: String,
    val partnerId: String,
    val bookingId: String
)