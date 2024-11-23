package controllers

import models.Movie
import models.Screening
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import updateScreening
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

    private var todayDateTime: LocalDateTime? = null
    private var todayDateTimePlusOneDays: LocalDateTime? = null
    private var todayDateTimePlusTwoDays: LocalDateTime? = null
    
    private var todayDate: LocalDate? = null
    private var todayDatePlusOneDays: LocalDate? = null
    private var todayDatePlusTwoDays: LocalDate? = null

    @BeforeEach
    fun setup() {
        // Movies
        paddington = Movie("Paddington", "Paul King", 95, "G", 1000)
        matrix = Movie("The Matrix", "Lana Wachowski, Lilly Wachowski", 136, "15A", 1001)
        gladiator = Movie("Gladiator", "Ridley Scott", 155, "16", 1002)
        robocop = Movie("Robocop", "Paul Verhoeven", 102, "18", 1003)
        casablanca = Movie( "Casablanca", "Michael Curtiz", 102, "G", 1004)

        // DateTimes and Dates
        todayDateTime = LocalDateTime.now()
        todayDateTimePlusOneDays = todayDateTime!!.plusDays(1)
        todayDateTimePlusTwoDays = todayDateTime!!.plusDays(2)
        
        todayDate = LocalDate.now()
        todayDatePlusOneDays = LocalDate.now().plusDays(1)
        todayDatePlusTwoDays = LocalDate.now().plusDays(2)
        
        // Screenings
        paddingtonScreening1 = Screening(paddington!!, todayDateTimePlusOneDays!!, 1)
        paddingtonScreening2 = Screening(paddington!!, todayDateTimePlusTwoDays!!, 2)
        matrixScreening1 = Screening(matrix!!, todayDateTime!!, 1)
        matrixScreening2 = Screening(matrix!!, todayDateTimePlusOneDays!!.plusHours(3), 2)
        robocopScreening1 = Screening(robocop!!, todayDateTimePlusTwoDays!!.plusHours(3), 3)

        // Robocop not added
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
        
        todayDateTime = null
        todayDateTimePlusOneDays = null
        todayDateTimePlusTwoDays = null
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
    inner class GetScreening {
        @Test
        fun `getScreening returns null when Screening ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertNull(emptyScreenings!!.getScreening(1000))
        }

        @Test
        fun `getScreening returns null when given a Screening ID that does not exist in Screening ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertNull(populatedScreenings!!.getScreening(9999))
        }

        @Test
        fun `getScreening returns specified Screening when given that screening's ID`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertNotNull(populatedScreenings!!.getScreening(1000))
            assertEquals(paddingtonScreening1, populatedScreenings!!.getScreening(1000))
        }
    }

    @Nested
    inner class AddScreening {
        @Test
        fun `Adding a Screening to a populated list adds to the arrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertNotNull(populatedScreenings!!.addScreening(robocopScreening1!!))
            assertEquals(5, populatedScreenings!!.numberOfScreenings())
            assertEquals(robocopScreening1, populatedScreenings!!.getScreening(1004))
        }

        @Test
        fun `Adding a Screening to an empty list adds to the ArrayList`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertTrue(emptyScreenings!!.addScreening(robocopScreening1!!))
            assertEquals(1, emptyScreenings!!.numberOfScreenings())
            assertEquals(robocopScreening1!!, emptyScreenings!!.getScreening(1000))
        }
    }

    @Nested
    inner class UpdateScreenings {
        @Test
        fun `updateScreening returns null when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertNull(emptyScreenings!!.updateScreening(1000, robocopScreening1!!))
        }
        
        @Test
        fun `updateScreening returns null when ArrayList contains no Screening with specified ID`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertNull(populatedScreenings!!.updateScreening(9999, robocopScreening1!!))
        }
        
        @Test
        fun `updateScreening returns true and updates the values, except ID, of specified Screening`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            val originalScreening = populatedScreenings!!.getScreening(1000)
            assertTrue(populatedScreenings!!.updateScreening(1000, robocopScreening1!!))
            val updatedScreening = populatedScreenings!!.getScreening(1000)
            
            assertNotEquals(originalScreening, updatedScreening)
            assertEquals(originalScreening!!.screeningID, updatedScreening!!.screeningID)
            assertEquals(robocop, updatedScreening.movie)
            assertEquals(robocopScreening1!!.screeningDateTime, updatedScreening.screeningDateTime)
            assertEquals(robocopScreening1!!.theatreNumber, updatedScreening.theatreNumber)
        }
    }

    @Nested
    inner class DeleteScreenings {
        @Test
        fun `deleteScreening returns null when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertNull(emptyScreenings!!.deleteScreening(1000))
        }
        
        @Test
        fun `deleteScreening returns null when ArrayList contains no Screening with specified ID`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertNull(populatedScreenings!!.deleteScreening(9999))
        }
        
        @Test
        fun `deleteScreening deletes specified Screening and returns the Screening if exists in ArrayList`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            val deletedScreening = populatedScreenings!!.deleteScreening(1000)
            assertNotNull(deletedScreening)
            assertEquals(paddingtonScreening1, deletedScreening)
        }
    }

    @Nested
    inner class ListScreenings {
        @Test
        fun `listAllScreenings returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), emptyScreenings!!.listAllScreenings())
        }

        @Test
        fun `listAllScreenings returns list of Screening strings when ArrayList is not empty`() {
            val screeningsList = populatedScreenings!!.listAllScreenings()
            assertEquals(screeningsList.size, populatedScreenings!!.numberOfScreenings())
            assertEquals(populatedScreenings!!.getScreening(1000).toString(), screeningsList[0])
            assertEquals(populatedScreenings!!.getScreening(1001).toString(), screeningsList[1])
            assertEquals(populatedScreenings!!.getScreening(1002).toString(), screeningsList[2])
            assertEquals(populatedScreenings!!.getScreening(1003).toString(), screeningsList[3])

        }

        @Test
        fun `listScreeningsByMovie returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), emptyScreenings!!.listScreeningsByMovie(1000))
        }

        @Test
        fun `listScreeningsByMovie returns empty string list when no Screenings of specified Movie exist`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), populatedScreenings!!.listScreeningsByMovie(1003))
        }

        @Test
        fun `listScreeningsByMovie returns list of Screening strings when Screenings of specified Movie exist`() {
            val paddingtonShowings = populatedScreenings!!.listScreeningsByMovie(1000)
            assertEquals(2, paddingtonShowings.size)
            assertTrue(paddingtonShowings[0].lowercase().contains("paddington"))
            assertTrue(paddingtonShowings[1].lowercase().contains("paddington"))
        }

        @Test
        fun `listScreeningsByDate returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), emptyScreenings!!.listScreeningsByDate(todayDatePlusOneDays!!))
        }

        @Test
        fun `listScreeningsByDate returns empty string list when no Screenings of specified date exist`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), populatedScreenings!!.listScreeningsByDate(todayDate!!.plusDays(4)))
        }

        @Test
        fun `listScreeningsByDate returns list of Screening strings when Screenings of specified date exist`() {
            val tomorrowShowings = populatedScreenings!!.listScreeningsByDate(todayDatePlusOneDays!!)
            assertEquals(2, tomorrowShowings.size)
            assertTrue(tomorrowShowings[0].lowercase().contains("paddington"))
            assertTrue(tomorrowShowings[1].lowercase().contains("matrix"))
        }

        @Test
        fun `listScreeningsByMovieAndDate returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), emptyScreenings!!.listScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!))
        }

        @Test
        fun `listScreeningsByMovieAndDate returns empty string list when no Screenings of specified movie and date exist`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), populatedScreenings!!.listScreeningsByMovieAndDate(1000, todayDate!!.plusDays(4)))
        }

        @Test
        fun `listScreeningsByMovieAndDate returns list of Screening strings when Screenings of specified movie and date exist`() {
            val tomorrowShowings = populatedScreenings!!.listScreeningsByMovieAndDate(1000, todayDatePlusOneDays!!)

            assertEquals(4, populatedScreenings!!.numberOfScreenings())
            assertEquals(1, tomorrowShowings.size)
            assertTrue(tomorrowShowings[0].lowercase().contains("paddington"))
        }

        @Test
        fun `listDayRemainingScreenings returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.numberOfScreenings())
            assertEquals(listOf<String>(), emptyScreenings!!.listDayRemainingScreenings())
        }

        @Test
        fun `listDayRemainingScreenings returns empty string list when there are no remaining Screenings of specified day`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())

            // Set all screening showings to 00:00
            var currentId = 1000
            for(i in 0 until populatedScreenings!!.numberOfScreenings()){
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))
            }

            val remainingScreenings = populatedScreenings!!.listDayRemainingScreenings()
            assertEquals(0, remainingScreenings.size)
            assertEquals(listOf<String>(), remainingScreenings)
        }

        @Test
        fun `listDayRemainingScreenings returns list of Screening strings when there are remaining Screenings of specified day`() {
            assertEquals(4, populatedScreenings!!.numberOfScreenings())

            // Set all screening showings to 23:59:59
            var currentId = 1000
            for(screening in 0 until populatedScreenings!!.numberOfScreenings()){
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))
            }

            val remainingScreenings = populatedScreenings!!.listDayRemainingScreenings()
            assertEquals(1, remainingScreenings)
            assertTrue(remainingScreenings[0].lowercase().contains("matrix"))
        }

        @Test
        fun `listDayRemainingScreeningsByMovie returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.listDayRemainingScreeningsByMovie(1002).size)
            assertEquals(listOf<String>(), emptyScreenings!!.listDayRemainingScreeningsByMovie(1002))
        }

        @Test
        fun `listDayRemainingScreeningsByMovie returns empty string list when there are no remaining Screenings of specified movie todayDate`() {
            // Setting only movie screening todayDate to 00:00:00
            populatedScreenings!!.getScreening(1001)!!.screeningDateTime =
                LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))

            assertEquals(0, populatedScreenings!!.listDayRemainingScreeningsByMovie(1001).size)
            assertEquals(listOf<String>(), populatedScreenings!!.listDayRemainingScreeningsByMovie(1001))
        }

        @Test
        fun `listDayRemainingScreeningsByMovie returns list of Screening strings when there are remaining Screenings of specified movie todayDate`() {
            // Setting only movie screening todayDate to 23:59:59
            populatedScreenings!!.getScreening(1001)!!.screeningDateTime =
                LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))

            val remainingScreenings = populatedScreenings!!.listDayRemainingScreeningsByMovie(1001)
            assertEquals(1, remainingScreenings.size)
            assertTrue(remainingScreenings[0].lowercase().contains("matrix"))
        }

        @Test
        fun `listWeekRemainingScreenings returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.listWeekRemainingScreenings().size)
            assertEquals(listOf<String>(), emptyScreenings!!.listWeekRemainingScreenings())
        }

        @Test
        fun `listWeekRemainingScreenings returns empty string list when there are no remaining Screenings this week`() {
            // Setting all movie screenings to 01/01 at 00:00
            var currentId = 1000
            for(screening in 0 until populatedScreenings!!.numberOfScreenings()){
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now().withDayOfYear(1),
                    LocalTime.of(0, 0, 0))
            }

            assertEquals(0, populatedScreenings!!.listWeekRemainingScreenings().size)
            assertEquals(listOf<String>(), populatedScreenings!!.listWeekRemainingScreenings())
        }

        @Test
        fun `listWeekRemainingScreenings returns list of Screening strings when there are remaining Screenings this week`() {
            // Setting all movie screenings to last day of week at 23:59:59
            var currentId = 1000
            for(screening in 0 until populatedScreenings!!.numberOfScreenings()){
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now().with(DayOfWeek.SUNDAY),
                        LocalTime.of(23, 59, 59))
            }

            val remainingScreenings = populatedScreenings!!.listWeekRemainingScreenings()
            assertEquals(4, remainingScreenings.size)
            assertTrue(remainingScreenings[0].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[1].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[2].lowercase().contains("matrix"))
            assertTrue(remainingScreenings[3].lowercase().contains("matrix"))
        }

        @Test
        fun `listWeekRemainingScreeningsByMovie returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.listWeekRemainingScreeningsByMovie(1000).size)
            assertEquals(listOf<String>(), emptyScreenings!!.listWeekRemainingScreeningsByMovie(1000))
        }

        @Test
        fun `listWeekRemainingScreeningsByMovie returns empty string list when there are no remaining Screenings of specified Movie this week`() {
            // Setting all movie screenings to 01/01 at 00:00
            var currentId = 1000
            for(screening in 0 until populatedScreenings!!.numberOfScreenings()){
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now().withDayOfYear(1),
                        LocalTime.of(0, 0, 0))
            }

            assertEquals(0, populatedScreenings!!.listWeekRemainingScreeningsByMovie(1000).size)
            assertEquals(listOf<String>(), populatedScreenings!!.listWeekRemainingScreeningsByMovie(1000))
        }

        @Test
        fun `listWeekRemainingScreeningsByMovie returns list of Screening strings when there are Screenings of specified Movie this week`() {
            // Setting all movie screenings to last day of week at 23:59:59
            var currentId = 1000
            for(screening in 0 until populatedScreenings!!.numberOfScreenings()){
                populatedScreenings!!.getScreening(currentId++)!!.screeningDateTime =
                    LocalDateTime.of(LocalDate.now().with(DayOfWeek.SUNDAY),
                        LocalTime.of(23, 59, 59))
            }

            val remainingScreenings = populatedScreenings!!.listWeekRemainingScreeningsByMovie(1000)
            assertEquals(2, remainingScreenings.size)
            assertTrue(remainingScreenings[0].lowercase().contains("paddington"))
            assertTrue(remainingScreenings[1].lowercase().contains("paddington"))
        }

        @Test
        fun `listAvailableSeats returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyScreenings!!.listAvailableSeats(1000).size)
            assertEquals(listOf<String>(), emptyScreenings!!.listAvailableSeats(1000))
        }

        @Test
        fun `listAvailableSeats returns empty string list when no seats are available for specified Screening`() {
            populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2", "A3"))

            assertEquals(0, populatedScreenings!!.listAvailableSeats(1000).size)
            assertEquals(listOf<String>(), populatedScreenings!!.listAvailableSeats(1000))
        }

        @Test
        fun `listAvailableSeats returns list of seat strings when seats are available for specified Screening`() {
            var availableSeats = populatedScreenings!!.listAvailableSeats(1000)
            assertEquals(3, availableSeats.size)
            assertEquals("A1", availableSeats[0])
            assertEquals("A2", availableSeats[1])
            assertEquals("A3", availableSeats[2])

            populatedScreenings!!.reserveSeats(1000, listOf("A1"))
            availableSeats = populatedScreenings!!.listAvailableSeats(1000)
            assertEquals(2, populatedScreenings!!.listAvailableSeats(1000).size)
            assertEquals("A2", availableSeats[0])
            assertEquals("A3", availableSeats[1])
        }
    }

    @Nested
    inner class CountingMovies {

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
            assertEquals(3, populatedScreenings!!.numberOfAvailableSeats(1000))
            populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2"))
            assertEquals(1, populatedScreenings!!.numberOfAvailableSeats(1000))
        }

        @Test
        fun numberOfSoldSeatsCalculatedCorrectly() {
            assertEquals(0, populatedScreenings!!.numberOfSoldSeats(1000))
            populatedScreenings!!.reserveSeats(1000, listOf("A1", "A2"))
            assertEquals(2, populatedScreenings!!.numberOfSoldSeats(1000))
        }
    }
}