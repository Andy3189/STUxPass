package de.hausdinga.stuxpass.repository.remote

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.hausdinga.stuxpass.local.database.Ticket
import de.hausdinga.stuxpass.remote.BearerAuth
import de.hausdinga.stuxpass.remote.User
import de.hausdinga.stuxpass.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class RideApiWebService @Inject constructor() : TicketWebService {
    private val api by lazy {
        createRideApi()
    }

    override suspend fun loadTickets(birthDate: String, immaNr: String): List<Ticket> {
        Log.d("[RIDE]", "LOAD TOKEN...")
        val token = api.login(User(birthDate, immaNr))
        Log.d("[RIDE]", "LOAD TOKEN DONE")
        val auth = BearerAuth(token.access_token)
        Log.d("[RIDE]", "LOAD BOOKING ID....")
        val bookingID = api.getBookingID(auth.getAuth())
        Log.d("[RIDE]", "LOAD BOOKING ID DONE")
        Log.d("[RIDE]", "SYNC TICKETS....")
        api.syncSemesterTickets(auth.getAuth())
        Log.d("[RIDE]", "SYNC TICKETS DONE")
        Log.d("[RIDE]", "LOAD PARTNER DATA....")
        val partnerData = api.loadPartnerData(Constants.TU_RIDE_PARTNER_ID)
        Log.d("[RIDE]", "LOAD PARTNER DATA DONE")
        Log.d("[RIDE]", "LOAD TICKETS....")
        val tickets = api.loadTickets(
            authorization = auth.getAuth(),
            partnerID = Constants.TU_RIDE_PARTNER_ID,
            bookingID = bookingID.bookingId
        )
        Log.d("[RIDE]", "LOAD TICKETS DONE")
        Log.d("[RIDE]", "LOAD TICKET EXTRA DATA....")
        val ticketExtraData = api.loadPartnerTicketData(
            authorization = auth.getAuth(),
            partnerID = Constants.TU_RIDE_PARTNER_ID
        )
        Log.d("[RIDE]", "LOAD TICKET EXTRA DATA DONE")
        return tickets.filter { it.status == Constants.TICKET_STATE_ACTIVE }.map { json ->
            Ticket(
                iconURLs = partnerData.ticketLogos.map { it.src.trim() },
                title = json.ticketName,
                validFrom = LocalDateTime.ofInstant(Instant.parse(json.validFrom), ZoneId.systemDefault()),
                validTo = LocalDateTime.ofInstant(Instant.parse(json.validTo), ZoneId.systemDefault()),
                validWhere = Constants.TICKET_VALID_WHERE,
                data = json.barcode,
                orderID = json.orderId,
                codeWord = json.codeWord,
                owner = "${json.preName} ${json.surName}",
                infoText = partnerData.ticketNoticeTextForTicketNames.deutschlandsemesterticket,
                aboNr = "${ticketExtraData.first { it.subscriptionReference.orderId == json.orderId }.aboNumber}"
            )
        }
    }

    private fun createRideApi(): RideApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(RideConstants.URL_BASE)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(RideApi::class.java)
    }
}