package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import utils.Utilities as Utils

class UtilitiesTest {
    @Test
    fun dateToYearsCalculatedCorrectly() {
        val eighteen = LocalDate.now().minusYears(18)
        val oneHundred = LocalDate.now().minusYears(100)
        val plusEighteen = LocalDate.now().plusYears(18)
        val plusOneHundred = LocalDate.now().plusYears(100)

        assertEquals(18, Utils.dateToYears(eighteen))
        assertEquals(100, Utils.dateToYears(oneHundred))
        assertEquals(-1, Utils.dateToYears(plusEighteen))
        assertEquals(-1, Utils.dateToYears(plusOneHundred))
    }
}
