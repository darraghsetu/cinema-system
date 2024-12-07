package controllers

import models.Movie
import models.Screening
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningAPITest {
    private var paddington: Movie? = null
    private var matrix: Movie? = null
    private var gladiator: Movie? = null
    private var robocop: Movie? = null
    private var casablanca: Movie? = null

    private var paddingtonScreening1: Screening? = null
    private var paddingtonScreening2: Screening? = null
    private var matrixScreening1: Screening? = null
    private var matrixScreening2: Screening? = null
    private var robocopScreening1: Screening? = null

    private var populatedScreenings: ScreeningAPI? = ScreeningAPI()
    private var emptyScreenings: ScreeningAPI? = ScreeningAPI()

    private var todayMiddayDateTime: LocalDateTime? = null
    private var todayMiddayDateTimePlusOneDays: LocalDateTime? = null
    private var todayMiddayDateTimePlusTwoDays: LocalDateTime? = null
    
    private var todayDate: LocalDate? = null
    private var todayDatePlusOneDays: LocalDate? = null
    private var todayDatePlusTwoDays: LocalDate? = null

    @BeforeEach
    fun setup() {
        // Movies
        val movies = MovieAPI()
        movies.addMovie(Movie("Paddington", "Paul King", 95, "G"))
        movies.addMovie(Movie("The Matrix", "Lana Wachowski, Lilly Wachowski", 136, "15A"))
        movies.addMovie(Movie("Gladiator", "Ridley Scott", 155, "16"))
        movies.addMovie(Movie("Robocop", "Paul Verhoeven", 102, "18"))
        movies.addMovie(Movie("Casablanca", "Michael Curtiz", 102, "G"))

        // Retrieved from API as the ID is set by addMovie
        paddington = movies.getMovie(1000)
        matrix = movies.getMovie(1001)
        gladiator = movies.getMovie(1002)
        robocop = movies.getMovie(1003)
        casablanca = movies.getMovie(1004)

        // DateTimes and Dates
        todayMiddayDateTime = LocalDateTime.of( LocalDate.now(), LocalTime.NOON )
        todayMiddayDateTimePlusOneDays = todayMiddayDateTime!!.plusDays(1)
        todayMiddayDateTimePlusTwoDays = todayMiddayDateTime!!.plusDays(2)
        
        todayDate = LocalDate.now()
        todayDatePlusOneDays = todayDate!!.plusDays(1)
        todayDatePlusTwoDays = todayDate!!.plusDays(2)
        
        // Screenings
        paddingtonScreening1 = Screening(paddington!!, todayMiddayDateTimePlusOneDays!!, 1)
        paddingtonScreening2 = Screening(paddington!!, todayMiddayDateTimePlusTwoDays!!, 2)
        matrixScreening1 = Screening(matrix!!, todayMiddayDateTime!!, 1)
        matrixScreening2 = Screening(matrix!!, todayMiddayDateTimePlusOneDays!!.plusHours(3), 2)
        robocopScreening1 = Screening(robocop!!, todayMiddayDateTimePlusTwoDays!!.plusHours(3), 3) // Not added

        populatedScreenings!!.addScreening(paddingtonScreening1!!)
        populatedScreenings!!.addScreening(paddingtonScreening2!!)
        populatedScreenings!!.addScreening(matrixScreening1!!)
        populatedScreenings!!.addScreening(matrixScreening2!!)
    }

    @AfterEach
    fun tearDown() {
        paddington = null
        matrix = null
        gladiator = null
        robocop = null
        casablanca = null
        
        todayMiddayDateTime = null
        todayMiddayDateTimePlusOneDays = null
        todayMiddayDateTimePlusTwoDays = null
        todayDate = null
        todayDatePlusOneDays = null
        todayDatePlusTwoDays = null
        
        paddingtonScreening1 = null
        paddingtonScreening2 = null
        matrixScreening1 = null
        matrixScreening2 = null
        robocopScreening1 = null
        
        populatedScreenings = null
        emptyScreenings = null
    }

    @Nested
    inner class AddScreening {
        @Test
        fun `addScreening returns true and adds Screening to an empty ArrayList`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertTrue(emptyScreenings!!.addScreening(robocopScreening1!!))
            assertTrue(emptyScreenings!!.hasScreenings())
            assertEquals(1, emptyScreenings!!.numberOfScreenings())
            assertEquals(robocopScreening1!!, emptyScreenings!!.getScreening(1000))
        }

        @Test
        fun `addScreening returns true and adds Screening to a populated ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.addScreening(robocopScreening1!!))
            assertEquals(5, populatedScreenings!!.numberOfScreenings())
            assertEquals(robocopScreening1, populatedScreenings!!.getScreening(1004))
        }
    }

    @Nested
    inner class GetScreening {
        @Test
        fun `getScreening returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.getScreening(1000))
        }

        @Test
        fun `getScreening returns null if the specified Screening ID does not exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.screeningExists(9999))
            assertNull(populatedScreenings!!.getScreening(9999))
        }

        @Test
        fun `getScreening returns Screening if the specified Screening ID exists in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.screeningExists(1000))
            assertNotNull(populatedScreenings!!.getScreening(1000))
            assertEquals(paddingtonScreening1, populatedScreenings!!.getScreening(1000))
        }

        @Test
        fun `getScreeningsByMovie returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.getScreeningsByMovie(1000))
        }

        @Test
        fun `getScreeningsByMovie returns null if Screenings of specified Movie do not exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.hasScreeningsByMovie(9999))
            assertNull(populatedScreenings!!.getScreeningsByMovie(9999))
        }

        @Test
        fun `getScreeningsByMovie returns list of Screenings if Screenings of specified Movie exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByMovie(1000))
            assertNotNull(populatedScreenings!!.getScreeningsByMovie(1000))
            assertEquals(
                listOf(paddingtonScreening1, paddingtonScreening2),
                populatedScreenings!!.getScreeningsByMovie(1000)
            )
        }

        @Test
        fun `getScreeningsByDate returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.getScreeningsByDate(todayDate!!))
        }

        @Test
        fun `getScreeningsByDate returns null if Screenings of specified Date do not exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.hasScreeningsByDate(LocalDate.MAX))
            assertNull(populatedScreenings!!.getScreeningsByDate(LocalDate.MAX))
        }

        @Test
        fun `getScreeningsByDate returns list of Screenings if Screenings of specified Date exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByDate(todayDate!!))
            assertNotNull(populatedScreenings!!.getScreeningsByDate(todayDate!!))
            assertEquals(
                listOf(matrixScreening1),
                populatedScreenings!!.getScreeningsByDate(todayDate!!)
            )
        }
    }

    @Nested
    inner class UpdateScreening {
        @Test
        fun `updateScreening returns false if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertFalse(emptyScreenings!!.updateScreening(1000, robocopScreening1!!))
        }
        
        @Test
        fun `updateScreening returns false if the specified Screening ID does not exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.screeningExists(9999))
            assertFalse(populatedScreenings!!.updateScreening(9999, robocopScreening1!!))
        }

        @Test
        fun `updateScreening returns true and updates the values, except ID, of specified Screening if it exists in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.screeningExists(1000))

            val originalID = populatedScreenings!!.getScreening(1000)!!.screeningID
            val originalMovie = populatedScreenings!!.getScreening(1000)!!.movie
            val originalDateTime = populatedScreenings!!.getScreening(1000)!!.screeningDateTime
            val originalTheatreID = populatedScreenings!!.getScreening(1000)!!.theatreID

            assertTrue(populatedScreenings!!.updateScreening(1000, robocopScreening1!!))
            val updatedScreening = populatedScreenings!!.getScreening(1000)!!

            assertEquals(originalID, updatedScreening.screeningID)
            assertNotEquals(originalMovie, updatedScreening.movie)
            assertNotEquals(originalDateTime, updatedScreening.screeningDateTime)
            assertNotEquals(originalTheatreID, updatedScreening.theatreID)
        }
    }

    @Nested
    inner class DeleteScreenings {
        @Test
        fun `deleteScreening returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.deleteScreening(1000))
        }
        
        @Test
        fun `deleteScreening returns null if the specified Screening ID does not exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.screeningExists(9999))
            assertNull(populatedScreenings!!.deleteScreening(9999))
        }
        
        @Test
        fun `deleteScreening deletes and returns Screening if the specified Screening ID exists in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.screeningExists(1000))
            assertNotNull(populatedScreenings!!.getScreening(1000))

            val countBefore = populatedScreenings!!.numberOfScreenings()
            val deletedScreening = populatedScreenings!!.deleteScreening(1000)
            val countAfter = populatedScreenings!!.numberOfScreenings()

            assertEquals(countBefore - 1, countAfter)
            assertFalse(populatedScreenings!!.screeningExists(1000))
            assertNull(populatedScreenings!!.getScreening(1000))
            assertNotNull(deletedScreening)
            assertEquals(paddingtonScreening1!!, deletedScreening!!)
        }

        @Test
        fun `deleteScreeningsByMovie returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.deleteScreeningsByMovie(1000))
        }

        @Test
        fun `deleteScreeningsByMovie returns null if no Screenings of specified Movie exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.hasScreeningsByMovie(9999))
            assertNull(populatedScreenings!!.deleteScreeningsByMovie(9999))
        }

        @Test
        fun `deleteScreeningsByMovie deletes and returns a list of deleted Screenings if Screenings of specified Movie exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByMovie(1000))
            assertNotNull(populatedScreenings!!.getScreeningsByMovie(1000))

            val countBefore = populatedScreenings!!.numberOfScreeningsByMovie(1000)
            val deletedScreenings = populatedScreenings!!.deleteScreeningsByMovie(1000)!!
            val countAfter = populatedScreenings!!.numberOfScreeningsByMovie(1000)
            assertEquals(countBefore - 2, countAfter)

            assertFalse(populatedScreenings!!.screeningExists(deletedScreenings[0].screeningID))
            assertFalse(populatedScreenings!!.screeningExists(deletedScreenings[1].screeningID))
            assertNull(populatedScreenings!!.getScreeningsByMovie(1000))
            assertNotNull(deletedScreenings)
            assertEquals(paddingtonScreening1!!, deletedScreenings[0])
            assertEquals(paddingtonScreening2!!, deletedScreenings[1])
        }

        @Test
        fun `deleteScreeningsByDate returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.deleteScreeningsByDate(todayDate!!))
        }

        @Test
        fun `deleteScreeningsByDate returns null if no Screenings of specified Date exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.hasScreeningsByDate(LocalDate.MAX))
            assertNull(populatedScreenings!!.deleteScreeningsByDate(LocalDate.MAX))
        }

        @Test
        fun `deleteScreeningsByDate deletes and returns a list of deleted Screenings if Screenings of specified Date exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByDate(todayDatePlusOneDays!!))
            assertNotNull(populatedScreenings!!.getScreeningsByDate(todayDatePlusOneDays!!))

            val countBefore = populatedScreenings!!.numberOfScreeningsByDate(todayDatePlusOneDays!!)
            val deletedScreenings = populatedScreenings!!.deleteScreeningsByDate(todayDatePlusOneDays!!)!!
            val countAfter = populatedScreenings!!.numberOfScreeningsByDate(todayDatePlusOneDays!!)
            assertEquals(countBefore - 2, countAfter)

            assertFalse(populatedScreenings!!.screeningExists(deletedScreenings[0].screeningID))
            assertFalse(populatedScreenings!!.screeningExists(deletedScreenings[1].screeningID))
            assertNull(populatedScreenings!!.getScreeningsByDate(todayDatePlusOneDays!!))
            assertNotNull(deletedScreenings)
            assertEquals(paddingtonScreening1!!, deletedScreenings[0])
            assertEquals(matrixScreening2!!, deletedScreenings[1])
        }
    }

    @Nested
    inner class ReservingSeats {
        @Test
        fun `reserveSeat returns false if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertFalse(emptyScreenings!!.reserveSeats(1000, listOf("A1", "A2")))
        }

        @Test
        fun `reserveSeat returns false if the specified Screening ID does not exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.screeningExists(9999))
            assertFalse(populatedScreenings!!.reserveSeats(9999, listOf("A1", "A2")))
        }

        @Test
        fun `reserveSeat returns false if the specified Screening does not have all specified seats`() {
            // Test for an invalid seat
            assertTrue(populatedScreenings!!.hasScreenings())
            assertEquals(15, populatedScreenings!!.numberOfAvailableSeats(1000))
            assertFalse(populatedScreenings!!.reserveSeats(1000, listOf("A99")))
            assertEquals(15, populatedScreenings!!.numberOfAvailableSeats(1000))
            
            // Test for valid seats that aren't available for reservation
            assertEquals(15, populatedScreenings!!.numberOfAvailableSeats(1000))
            assertTrue(populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2", "A3", "A4")))
            assertEquals(11, populatedScreenings!!.numberOfAvailableSeats(1000))
            
            assertFalse(populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2", "A3", "A4")))
            assertEquals(11, populatedScreenings!!.numberOfAvailableSeats(1000))
        }

        @Test
        fun `reserveSeat returns true and also removes specified seats if Screening has all seats`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertEquals(15, populatedScreenings!!.numberOfAvailableSeats(1000))
            assertTrue(populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2", "A3", "A4")))
            assertEquals(11, populatedScreenings!!.numberOfAvailableSeats(1000))
            assertFalse(populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2", "A3", "A4")))
            assertEquals(11, populatedScreenings!!.numberOfAvailableSeats(1000))
        }
    }

    @Nested
    inner class ListingScreenings {
        @Test
        fun `listAllScreenings returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.listAllScreenings())
        }

        @Test
        fun `listAllScreenings returns list of Screening strings if the ArrayList is not empty`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            val screeningsList = populatedScreenings!!.listAllScreenings()
            assertEquals(populatedScreenings!!.numberOfScreenings(), screeningsList!!.size)
            assertEquals(populatedScreenings!!.getScreening(1000).toString(), screeningsList[0])
            assertEquals(populatedScreenings!!.getScreening(1001).toString(), screeningsList[1])
            assertEquals(populatedScreenings!!.getScreening(1002).toString(), screeningsList[2])
            assertEquals(populatedScreenings!!.getScreening(1003).toString(), screeningsList[3])
        }

        @Test
        fun `listScreeningsByMovie returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.listScreeningsByMovie(1000))
        }

        @Test
        fun `listScreeningsByMovie returns null if no Screenings of specified Movie exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.hasScreeningsByMovie(1003))
            assertNull(populatedScreenings!!.listScreeningsByMovie(1003))
        }

        @Test
        fun `listScreeningsByMovie returns list of Screening strings if Screenings of specified Movie exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreeningsByMovie(1000))
            val paddingtonShowings = populatedScreenings!!.listScreeningsByMovie(1000)
            assertEquals(populatedScreenings!!.numberOfScreeningsByMovie(1000), paddingtonShowings!!.size)
            assertTrue(paddingtonShowings[0].lowercase().contains("paddington"))
            assertTrue(paddingtonShowings[1].lowercase().contains("paddington"))
        }

        @Test
        fun `listScreeningsByDate returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.listScreeningsByDate(todayDate!!))
        }

        @Test
        fun `listScreeningsByDate returns null if no Screenings of specified Date exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.hasScreeningsByDate(todayDate!!.plusDays(4)))
            assertNull(populatedScreenings!!.listScreeningsByDate(todayDate!!.plusDays(4)))
        }

        @Test
        fun `listScreeningsByDate returns list of Screening strings if Screenings of specified Date exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreeningsByDate(todayDatePlusOneDays!!))
            val tomorrowShowings = populatedScreenings!!.listScreeningsByDate(todayDatePlusOneDays!!)
            assertEquals(
                populatedScreenings!!.numberOfScreeningsByDate(todayDatePlusOneDays!!),
                tomorrowShowings!!.size
            )
            assertTrue(tomorrowShowings[0].lowercase().contains("paddington"))
        }

        @Test
        fun `listScreeningsByMovieAndDate returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.listScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!)
            )
        }

        @Test
        fun `listScreeningsByMovieAndDate returns null if no Screenings of specified Movie and Date exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertFalse(populatedScreenings!!.hasScreeningsByMovieAndDate(1000, todayDate!!))
            assertNull(populatedScreenings!!.listScreeningsByMovieAndDate(1000, todayDate!!))
        }

        @Test
        fun `listScreeningsByMovieAndDate returns list of Screening strings if Screenings of specified Movie and Date exist in the ArrayList`() {
            assertTrue(populatedScreenings!!.hasScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!))
            val tomorrowPaddingtonShowings =
                populatedScreenings!!.listScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!)
            assertEquals(
                populatedScreenings!!.numberOfScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!),
                tomorrowPaddingtonShowings!!.size
            )
            assertTrue(tomorrowPaddingtonShowings[0].lowercase().contains("paddington"))
        }

        @Test
        fun `listDayRemainingScreenings returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.listDayRemainingScreenings())
        }

        @Test
        fun `listDayRemainingScreenings returns null if there are no remaining Screenings today`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByDate(todayDate!!))

            // Set all showing times to 00:00
            var currentId = 1000
            for (i in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
            }

            assertNull(populatedScreenings!!.listDayRemainingScreenings())
        }

        @Test
        fun `listDayRemainingScreenings returns list of Screening strings if there are remaining Screenings today`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByDate(todayDate!!))

            // Set all screening showings to today 23:59:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))
            }

            val remainingScreenings = populatedScreenings!!.listDayRemainingScreenings()!!
            assertEquals(
                populatedScreenings!!.numberOfScreeningsByDate(todayDate!!),
                remainingScreenings.size
            )

            assertTrue(remainingScreenings[0].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[1].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[2].lowercase().contains("matrix"))
            assertTrue(remainingScreenings[3].lowercase().contains("matrix"))
        }

        @Test
        fun `listDayRemainingScreeningsByMovie returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasScreenings())
            assertNull(emptyScreenings!!.listDayRemainingScreeningsByMovie(1002))
        }

        @Test
        fun `listDayRemainingScreeningsByMovie returns null if there are no remaining Screenings of specified Movie today`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByMovieAndDate(1001, todayDate!!))

            // Set all showing times to today at 00:00
            var currentId = 1000
            for (i in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
            }

            assertNull(populatedScreenings!!.listDayRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `listDayRemainingScreeningsByMovie returns list of Screening strings if there are remaining Screenings of specified Movie today`() {
            assertTrue(populatedScreenings!!.hasScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByMovieAndDate(1001, todayDate!!))

            // Set all screening showings to today 23:59:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))
            }

            val remainingScreenings = populatedScreenings!!.listDayRemainingScreeningsByMovie(1001)
            assertEquals(
                populatedScreenings!!.numberOfScreeningsByMovieAndDate(1001, todayDate!!),
                remainingScreenings!!.size
            )
            assertTrue(remainingScreenings[0].lowercase().contains("matrix"))
        }

        @Test
        fun `listWeekRemainingScreenings returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasWeekRemainingScreenings())
            assertNull(emptyScreenings!!.listWeekRemainingScreenings())
        }

        @Test
        fun `listWeekRemainingScreenings returns null if there are no remaining Screenings this week`() {
            assertTrue(populatedScreenings!!.hasScreenings())

            // Setting all Movie screenings Date to before this week
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.of(1970, 1, 1),
                        LocalTime.of(0, 0)
                    )
            }

            assertFalse(populatedScreenings!!.hasWeekRemainingScreenings())
            assertNull(populatedScreenings!!.listWeekRemainingScreenings())
        }

        @Test
        fun `listWeekRemainingScreenings returns list of Screening strings if there are remaining Screenings this week`() {
            // Setting all Screenings to last day of week at 23:59:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.now().with(DayOfWeek.SUNDAY),
                        LocalTime.of(23, 59, 59)
                    )
            }

            val remainingScreenings = populatedScreenings!!.listWeekRemainingScreenings()
            assertEquals(4, remainingScreenings!!.size)
            assertTrue(remainingScreenings[0].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[1].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[2].lowercase().contains("matrix"))
            assertTrue(remainingScreenings[3].lowercase().contains("matrix"))
        }

        @Test
        fun `listWeekRemainingScreeningsByMovie returns null if the ArrayList is empty`() {
            assertFalse(emptyScreenings!!.hasWeekRemainingScreeningsByMovie(1000))
            assertNull(emptyScreenings!!.listWeekRemainingScreeningsByMovie(1000))
        }

        @Test
        fun `listWeekRemainingScreeningsByMovie returns null if there are no remaining Screenings of specified Movie this week`() {
            assertTrue(populatedScreenings!!.hasScreenings())

            // Setting all Movie screenings Date to before this week
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.of(1970, 1, 1),
                        LocalTime.of(0, 0)
                    )
            }

            assertTrue(populatedScreenings!!.hasScreeningsByMovie(1000))
            assertFalse(populatedScreenings!!.hasWeekRemainingScreeningsByMovie(1000))
            assertNull(populatedScreenings!!.listWeekRemainingScreeningsByMovie(1000))
        }

        @Test
        fun `listWeekRemainingScreeningsByMovie returns list of Screening strings if there are Screenings of specified Movie this week`() {
            // Setting all Movie screenings to last day of week at 23:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.now().with(DayOfWeek.SUNDAY),
                        LocalTime.of(23, 59)
                    )
            }

            val remainingScreenings = populatedScreenings!!.listWeekRemainingScreeningsByMovie(1000)
            assertEquals(2, remainingScreenings!!.size)
            assertTrue(remainingScreenings[0].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[1].lowercase().contains("paddington"))
        }

        @Test
        fun `listAvailableSeats returns null if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertNull(emptyScreenings!!.listAvailableSeats(1000))
        }

        @Test
        fun `listAvailableSeats returns null if no seats are available for specified Screening`() {
            populatedScreenings!!.reserveSeats(
                1000,
                listOf(
                    "A1", "A2", "A3", "A4", "A5",
                    "B1", "B2", "B3", "B4", "B5",
                    "C1", "C2", "C3", "C4", "C5",
                )
            )

            assertTrue(populatedScreenings!!.screeningExists(1000))
            assertNull(populatedScreenings!!.listAvailableSeats(1000))
        }

        @Test
        fun `listAvailableSeats returns list of seat strings if there are seats available for specified Screening`() {
            var availableSeats = populatedScreenings!!.listAvailableSeats(1000)!!
            assertEquals(15, availableSeats.size)
            assertEquals("A1", availableSeats[0])
            assertEquals("A2", availableSeats[1])
            assertEquals("A3", availableSeats[2])

            populatedScreenings!!.reserveSeats(1000, listOf("A1"))
            availableSeats = populatedScreenings!!.listAvailableSeats(1000)!!
            assertEquals(14, populatedScreenings!!.listAvailableSeats(1000)!!.size)
            assertEquals("A2", availableSeats[0])
            assertEquals("A3", availableSeats[1])
        }
    }

    @Nested
    inner class CountingScreenings {
        @Test
        fun numberOfScreeningsCalculatedCorrectly() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
        }

        @Test
        fun numberOfScreeningsByMovieCalculatedCorrectly() {
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByMovie(1000))
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByMovie(1000))
        }

        @Test
        fun numberOfScreeningsByDateCalculatedCorrectly() {
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByDate(todayDatePlusOneDays!!))
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByDate(todayDatePlusOneDays!!))
        }

        @Test
        fun numberOfScreeningsByMovieAndDateCalculatedCorrectly() {
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!))
            assertEquals(1, populatedScreenings!!.numberOfScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!))
        }

        @Test
        fun numberOfAvailableSeatsCalculatedCorrectly() {
            assertEquals(0, emptyScreenings!!.numberOfAvailableSeats(1000))
            assertEquals(15, populatedScreenings!!.numberOfAvailableSeats(1000))
            populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2"))
            assertEquals(13, populatedScreenings!!.numberOfAvailableSeats(1000))
        }

        @Test
        fun numberOfSoldSeatsCalculatedCorrectly() {
            assertEquals(0, emptyScreenings!!.numberOfSoldSeats(1000))
            assertEquals(0, populatedScreenings!!.numberOfSoldSeats(1000))
            assertTrue(populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2")))
            assertEquals(2, populatedScreenings!!.numberOfSoldSeats(1000))
        }
    }

    @Nested
    inner class BooleanQueries {
        @Test
        fun `hasScreenings returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertFalse(emptyScreenings!!.hasScreenings())
        }

        @Test
        fun `hasScreenings returns true if there are Screenings in the arraylist`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.hasScreenings())
        }

        @Test
        fun `hasScreeningsByMovie returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByMovie(1000))
            assertFalse(emptyScreenings!!.hasScreeningsByMovie(1000))
        }

        @Test
        fun `hasScreeningsByMovie returns false if there are no Screenings of specified Movie in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(0, populatedScreenings!!.numberOfScreeningsByMovie(robocop!!.movieID))
            assertFalse(populatedScreenings!!.hasScreeningsByMovie(robocop!!.movieID))
        }

        @Test
        fun `hasScreeningsByMovie returns true if there are Screenings of specified Movie in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByMovie(paddington!!.movieID))
            assertTrue(populatedScreenings!!.hasScreeningsByMovie(paddington!!.movieID))
        }

        @Test
        fun `hasScreeningByDate returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByDate(todayDate!!))
            assertFalse(emptyScreenings!!.hasScreeningsByDate(todayDate!!))
        }

        @Test
        fun `hasScreeningByDate returns false if there are no Screenings of specified Date in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(0, populatedScreenings!!.numberOfScreeningsByDate(todayDate!!.plusDays(4)))
            assertFalse(populatedScreenings!!.hasScreeningsByDate(todayDate!!.plusDays(4)))
        }

        @Test
        fun `hasScreeningByDate true if there are Screenings of specified Date in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByDate(todayDatePlusOneDays!!))
            assertTrue(populatedScreenings!!.hasScreeningsByDate(todayDatePlusOneDays!!))
        }

        @Test
        fun `hasScreeningByMovieAndDate returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!))
            assertFalse(emptyScreenings!!.hasScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!))
        }

        @Test
        fun `hasScreeningByMovieAndDate returns false if there are no Screenings of specified Movie or Date in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByMovie(1000))
            assertEquals(1, populatedScreenings!!.numberOfScreeningsByDate(todayDate!!))
            assertEquals(0, populatedScreenings!!.numberOfScreeningsByMovieAndDate(1000, todayDate!!))
            assertFalse(populatedScreenings!!.hasScreeningsByMovieAndDate(1000, todayDate!!))
        }

        @Test
        fun `hasScreeningByMovieAndDate returns true if there are Screenings of specified Movie and Date in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByMovie(1001))
            assertEquals(1, populatedScreenings!!.numberOfScreeningsByDate(todayDate!!))
            assertEquals(1, populatedScreenings!!.numberOfScreeningsByMovieAndDate(1001, todayDate!!))
            assertTrue(populatedScreenings!!.hasScreeningsByMovieAndDate(1001, todayDate!!))
        }

        @Test
        fun `hasDayRemainingScreenings returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByDate(todayDate!!))
            assertFalse(emptyScreenings!!.hasDayRemainingScreenings())
        }

        @Test
        fun `hasDayRemainingScreenings returns false if there are no Screenings remaining for today`() {
            // Set all showing times to today at 00:00
            var currentId = 1000
            for (i in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByDate(todayDate!!))
            assertFalse(populatedScreenings!!.hasDayRemainingScreenings())
        }

        @Test
        fun `hasDayRemainingScreenings returns true if there are Screenings remaining today`() {
            // Set all screening showings to today 23:59:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByDate(todayDate!!))
            assertTrue(populatedScreenings!!.hasDayRemainingScreenings())
        }

        @Test
        fun `hasDayRemainingScreeningsByMovie returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByDate(todayDate!!))
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByMovieAndDate(1001, todayDate!!))
            assertFalse(emptyScreenings!!.hasDayRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `hasDayRemainingScreeningsByMovie returns false if there are no remaining Screenings today of specified Movie`() {
            // Set all showing times to today at 00:00
            var currentId = 1000
            for (i in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByMovieAndDate(1001, todayDate!!))
            assertFalse(populatedScreenings!!.hasDayRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `hasDayRemainingScreeningsByMovie returns true if there are remaining Screenings today of specified Movie`() {
            // Set all screening showings to today 23:59:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.hasScreeningsByMovieAndDate(1001, todayDate!!))
            assertTrue(populatedScreenings!!.hasDayRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `hasWeekRemainingScreenings returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertFalse(emptyScreenings!!.hasWeekRemainingScreenings())
        }

        @Test
        fun `hasWeekRemainingScreenings returns false if there are no remaining Screenings this week`() {
            // Setting all Movie screenings Date to before this week
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.of(1970, 1, 1),
                        LocalTime.of(0, 0)
                    )
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertFalse(populatedScreenings!!.hasWeekRemainingScreenings())
        }

        @Test
        fun `hasWeekRemainingScreenings returns true if there are remaining Screenings this week`() {
            // Setting all Movie screenings to last day of week at 23:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.now().with(DayOfWeek.SUNDAY),
                        LocalTime.of(23, 59)
                    )
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.hasWeekRemainingScreenings())
        }

        @Test
        fun `hasWeekRemainingScreeningsByMovie returns false if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(0, emptyScreenings!!.numberOfScreeningsByMovie(1001))
            assertFalse(emptyScreenings!!.hasWeekRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `hasWeekRemainingScreeningsByMovie returns false if there are no remaining Screenings this week of specified Movie`() {
            // Setting all Movie screenings Date to before this week
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.of(1970, 1, 1),
                        LocalTime.of(0, 0)
                    )
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByMovie(1001))
            assertFalse(populatedScreenings!!.hasWeekRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `hasWeekRemainingScreeningsByMovie returns true if there are remaining Screenings this week of specified Movie`() {
            // Setting all Movie screenings to last day of week at 23:59
            var currentId = 1000
            for (screening in 0 until populatedScreenings!!.numberOfScreenings()) {
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(
                        LocalDate.now().with(DayOfWeek.SUNDAY),
                        LocalTime.of(23, 59)
                    )
            }

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(2, populatedScreenings!!.numberOfScreeningsByMovie(1001))
            assertTrue(populatedScreenings!!.hasWeekRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `screeningExists returns false if ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertFalse(emptyScreenings!!.screeningExists(1000))
        }

        @Test
        fun `screeningExists returns false if the specified Screening ID does not exist in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertFalse(populatedScreenings!!.screeningExists(9999))
        }

        @Test
        fun `screeningExists returns true if the specified Screening ID does exist in the ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertTrue(populatedScreenings!!.screeningExists(1000))
            assertTrue(populatedScreenings!!.screeningExists(1001))
            assertTrue(populatedScreenings!!.screeningExists(1002))
            assertTrue(populatedScreenings!!.screeningExists(1003))
        }

        @Test
        fun `theatreExists returns false if the specified Theatre ID does not exist`() {
            assertFalse(populatedScreenings!!.theatreExists(9999))
        }

        @Test
        fun `theatreExists returns true if the specified Theatre ID does exist`() {
            assertTrue(emptyScreenings!!.theatreExists(1))
            assertTrue(populatedScreenings!!.theatreExists(1))
            assertTrue(populatedScreenings!!.theatreExists(2))
            assertTrue(populatedScreenings!!.theatreExists(3))
            assertFalse(populatedScreenings!!.theatreExists(4))
        }

        @Test
        fun `availableDateTime returns false if the specified Date and Time for Theatre ID are not available`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())

            // 1. Having a start time before another screening has ended
            assertFalse(
                populatedScreenings!!.availableDateTime(
                    matrixScreening1!!.screeningDateTime.plusHours(1),
                    matrixScreening1!!.movie.runtime, //Matrix Runtime: 136 min
                    matrixScreening1!!.theatreID
                )
            )

            // 2. Having an end time after another screening has started
            assertFalse(
                populatedScreenings!!.availableDateTime(
                    matrixScreening1!!.screeningDateTime.minusHours(1),
                    matrixScreening1!!.movie.runtime,
                    matrixScreening1!!.theatreID
                )
            )

            // 3. Starting and ending between another screening
            assertFalse(
                populatedScreenings!!.availableDateTime(
                    matrixScreening1!!.screeningDateTime.plusMinutes(10),
                    runtime = 10,
                    matrixScreening1!!.theatreID
                )
            )

            // 4. Starts before opening time or after closing time
            assertFalse(
                populatedScreenings!!.availableDateTime(
                    LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(8, 59)
                    ),
                    runtime = 60,
                    theatreID = 1
                )
            )

            assertFalse(
                populatedScreenings!!.availableDateTime(
                    LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(23, 1)
                    ),
                    runtime = 60,
                    theatreID = 1
                )
            )

            // 5. Negative runtime
            assertFalse(
                populatedScreenings!!.availableDateTime(
                    LocalDateTime.of(
                        todayDate!!,
                        LocalTime.of(16, 0)
                    ),
                    runtime = -60,
                    theatreID = 1
                )
            )

            // 5. Invalid theatreID
            assertFalse(
                populatedScreenings!!.availableDateTime(
                    LocalDateTime.of(
                        todayDate!!,
                        LocalTime.of(16, 0)
                    ),
                    runtime = 60,
                    theatreID = 9999
                )
            )
        }

        @Test
        fun `availableDateTime returns True if the ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertTrue(emptyScreenings!!.availableDateTime(todayMiddayDateTime!!, 90, 1))
        }

        @Test
        fun `availableDateTime returns true if the specified Date and Time for theatreID are available`() {
            // Same theatre, non-clashing time
            assertTrue(
                populatedScreenings!!.availableDateTime(
                    matrixScreening1!!.screeningDateTime.plusHours(3),
                    runtime = 90,
                    theatreID = 1
                )
            )

            assertTrue(
                populatedScreenings!!.availableDateTime(
                    matrixScreening1!!.screeningDateTime.minusHours(2),
                    runtime = 90,
                    theatreID = 1
                )
            )

            // Same time, non-clashing theatre
            assertTrue(
                populatedScreenings!!.availableDateTime(
                    matrixScreening1!!.screeningDateTime,
                    runtime = 120,
                    theatreID = 2
                )
            )
        }

    }

}