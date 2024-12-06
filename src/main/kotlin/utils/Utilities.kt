package utils

import java.time.LocalDate
import java.time.Period

object Utilities {

    @JvmStatic
    @Suppress("RegExpRedundantEscape")
    fun isValidEmail(email: String) =
        /*
         * NOT ORIGINAL WORK:
         * This regular expression is not my own work,
         * it was obtained from: https://www.emailregex.com/
         */
        Regex(
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+" +
                    ")*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7" +
                    "f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-" +
                    "z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0" +
                    "-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]" +
                    "?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1" +
                    "f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        ).matches(email)

    @JvmStatic
    fun dateOfBirthToAge(dob: LocalDate) =
        Period.between(dob, LocalDate.now()).years
}
