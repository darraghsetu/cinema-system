package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Customer(
    var fName: String,
    var lName: String,
    var email: String,
    var dob: LocalDate
) {
    internal var customerID = 0
    internal var isAdult = false

    override fun toString(): String {
        val date = dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val textAreaWidth = 50
        val formatString = "%-${textAreaWidth}s"

        return """
        | │ ${String.format(formatString, "(ID: $customerID)")} │
        | │ ${String.format(formatString, "Name: $fName $lName")} │
        | │ ${String.format(formatString, "Date of Birth: $date")} │
        | │ ${String.format(formatString, "Email: $email")} │
        """.trimMargin()
    }
}
