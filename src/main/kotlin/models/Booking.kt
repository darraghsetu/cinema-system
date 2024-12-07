package models

import java.time.format.DateTimeFormatter

data class Booking(
    var screening: Screening,
    var customer: Customer,
    var numberOfTickets: Int,
    var salePrice: Double,
    var seats: List<String>,
) {
    internal var bookingID = 0
    internal var cancelled = false

    @Override
    override fun toString() =
        "(ID: $bookingID) " +
        (if(cancelled) "CANCELLED b" else "B") +
        "ooking for ${customer.fName} ${customer.lName}. " +
        "${screening.movie.title} (${screening.movie.certification}) on " +
        "${screening.screeningDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} at " +
        "${screening.screeningDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))}. \n" +
        "Seats: ${seats.joinToString(separator = ", ")}."
}
