package utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Utilities {

    @JvmStatic
    fun printStringList(list: List<String>) {
        list.forEach { println(" $it") }
        println()
    }

    @JvmStatic
    fun dateToYears(date: LocalDate) =
        if (date < LocalDate.now()) {
            Period.between(date, LocalDate.now()).years
        } else {
            -1
        }

    @JvmStatic
    fun getValidDate(prompt: String, pattern: String): LocalDate {
        var date: LocalDate? = null

        while (date == null) {
            date = isValidDate(
                ScannerInput.readNextLine(prompt),
                pattern
            )

            if (date == null) println(" Invalid date entered")
        }

        return date
    }

    @JvmStatic
    private fun isValidDate(date: String, pattern: String) =
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern))
        } catch (d: DateTimeParseException) {
            null
        }

    @JvmStatic
    fun getValidTime(prompt: String, pattern: String): LocalTime {
        var time: LocalTime? = null

        while (time == null) {
            time = isValidTime(
                ScannerInput.readNextLine(prompt),
                pattern
            )

            if (time == null) println(" Invalid time entered")
        }

        return time
    }

    private fun isValidTime(time: String, pattern: String) =
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern))
        } catch (d: DateTimeParseException) {
            null
        }

    fun getValidEmail(prompt: String): String {
        var email = ""
        var validEmail = false

        while (!validEmail) {
            email = ScannerInput.readNextLine(prompt)
            validEmail = isValidEmail(email)

            if (!validEmail) println(" Invalid email entered")
        }

        return email
    }

    /*
     * NOT ORIGINAL WORK:
     * The regular expression within is not my own work,
     * it was obtained from: https://www.emailregex.com/
     */
    @JvmStatic
    @Suppress("RegExpRedundantEscape")
    private fun isValidEmail(email: String) =
        Regex(
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+" +
                ")*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7" +
                "f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-" +
                "z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0" +
                "-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]" +
                "?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1" +
                "f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        ).matches(email)
}
