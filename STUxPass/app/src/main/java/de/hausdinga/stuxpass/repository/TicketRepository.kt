package de.hausdinga.stuxpass.repository

import de.hausdinga.stuxpass.local.database.Ticket
import de.hausdinga.stuxpass.local.database.TicketDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface TicketRepository {
    val tickets: Flow<List<Ticket>>
    val ticket: Flow<Ticket?>

    suspend fun load(orderID: String): Ticket?
    suspend fun add(ticket: Ticket)
    suspend fun delete(ticket: Ticket)
    suspend fun clear()
}

class DefaultTicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) : TicketRepository {

    override val tickets: Flow<List<Ticket>> = ticketDao.getTickets()
    override val ticket: Flow<Ticket?> = flowOf(null)

    override suspend fun load(orderID: String): Ticket? {
        return ticketDao.load(orderID)
    }

    override suspend fun add(ticket: Ticket) {
        ticketDao.insertTicket(ticket)
    }

    override suspend fun delete(ticket: Ticket) {
        ticketDao.deleteTicket(ticket)
    }

    override suspend fun clear() {
        ticketDao.dropTickets()
    }
}
