package de.hausdinga.stuxpass.repository.remote

import de.hausdinga.stuxpass.remote.BookingID
import de.hausdinga.stuxpass.remote.PartnerJson
import de.hausdinga.stuxpass.remote.PartnerTicketJson
import de.hausdinga.stuxpass.remote.TicketJson
import de.hausdinga.stuxpass.remote.User
import de.hausdinga.stuxpass.remote.UserToken
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RideApi {
    @POST(RideConstants.URL_LOGIN)
    suspend fun login(@Body user: User): UserToken

    @GET(RideConstants.URL_BOOKING_ID)
    suspend fun getBookingID(@Header(RideConstants.HEADER_AUTH) authorization: String): BookingID

    @POST(RideConstants.URL_TICKET_SYNC)
    suspend fun syncSemesterTickets(@Header(RideConstants.HEADER_AUTH) authorization: String)

    @GET(RideConstants.URL_PARTNER)
    suspend fun loadPartnerData(@Path(RideConstants.PARAM_PARTNER_ID) partnerID: String): PartnerJson

    @GET(RideConstants.URL_TICKET_COLLECTION)
    suspend fun loadPartnerTicketData(@Header(RideConstants.HEADER_AUTH) authorization: String, @Path(RideConstants.PARAM_PARTNER_ID) partnerID: String): List<PartnerTicketJson>

    @GET(RideConstants.URL_TICKET_DATA)
    suspend fun loadTickets(@Header(RideConstants.HEADER_AUTH) authorization: String, @Path(RideConstants.PARAM_PARTNER_ID) partnerID: String,@Path(RideConstants.PARAM_BOOKING_ID) bookingID: String): List<TicketJson>
}