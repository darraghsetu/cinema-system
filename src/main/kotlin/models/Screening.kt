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

    override fun toString(): String {
        val textAreaWidth = 50
        val formatString = "%-${textAreaWidth}s"
        val date = screeningDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy"))
        val time = screeningDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

        return """
        | │ ${String.format(formatString, "(ID: $screeningID)")} │
        | │ ${String.format(formatString, "${movie.title} (${movie.certification})")} │
        | │ ${String.format(formatString, "Date: $date")} │
        | │ ${String.format(formatString, "Time: $time")} │
        | │ ${String.format(formatString, "Theatre: $theatreID")} │
        """.trimMargin()
    }
}
