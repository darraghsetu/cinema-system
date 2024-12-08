package models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Screening(
    var movie: Movie,
    var screeningDateTime: LocalDateTime,
    var theatreID: Int
) {
    internal var screeningID = 0
    internal var seats = mutableListOf<String>()

    override fun toString() =
        "(ID: $screeningID) ${movie.title} (${movie.certification}) " +
            "on ${screeningDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy"))} " +
            "at ${screeningDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))} in " +
            "Theatre $theatreID"
}
