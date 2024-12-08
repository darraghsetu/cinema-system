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

    override fun toString() =
        "(ID: $customerID) $fName $lName. " +
            "Date of Birth: ${dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}. " +
            "Email: $email"
}
