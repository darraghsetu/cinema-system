package models

import java.time.format.DateTimeFormatter

data class Booking(
    var screening: Screening,
    var customer: Customer,
    var numberOfTickets: Int,
    var salePrice: Double,
    var seats: List<String>
) {
    internal var bookingID = 0
    internal var cancelled = false

    @Override
    override fun toString(): String {
        val date = screening.screeningDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val time = screening.screeningDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        val cancelledString = if (cancelled) "CANCELLED booking" else "Booking"
        val total = String.format("%.2f", salePrice)
        val textAreaWidth = 50
        val formatString = "%-${textAreaWidth}s"

        return """
        | │ ${String.format(formatString, "(ID: $bookingID)")} │
        | │ ${String.format(formatString, "$cancelledString for ${customer.fName} ${customer.lName}.")} │ 
        | │ ${String.format(formatString, "${screening.movie.title} (${screening.movie.certification})")} │
        | │ ${String.format(formatString, "Date: $date")} │
        | │ ${String.format(formatString, "Time: $time")} │
        | │ ${String.format(formatString, "Tickets: $numberOfTickets (Total: €$total")} │
        | │ ${String.format(formatString, "Seats: ${seats.joinToString(separator = ", ")}")} │  
        """.trimMargin()
    }
}
