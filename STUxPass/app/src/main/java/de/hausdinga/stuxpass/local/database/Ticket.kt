package de.hausdinga.stuxpass.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Entity
data class Ticket(
    val iconURLs: List<String>,
    val title: String,
    val validFrom: LocalDateTime,
    val validTo: LocalDateTime,
    val validWhere: String,
    val data: String,
    @PrimaryKey val orderID: String,
    val codeWord: String,
    val owner: String,
    val infoText: String,
    val aboNr: String,
    val valid: Boolean = validTo.isAfter(LocalDateTime.now())
)

@Dao
interface TicketDao {
    @Query("SELECT * FROM Ticket ORDER BY orderID DESC LIMIT 10")
    fun getTickets(): Flow<List<Ticket>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(item: Ticket)

    @Delete
    suspend fun deleteTicket(ticket: Ticket)

    @Query("DELETE FROM Ticket")
    suspend fun dropTickets()

    @Query("SELECT * FROM TICKET WHERE orderID = :orderID")
    suspend fun load(orderID: String): Ticket?
}
