package controllers

import models.Screening
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningAPI {
    private val screenings = ArrayList<Screening>()
    private val openingTime = LocalTime.of(9, 0)
    private val closingTime = LocalTime.of(23, 0)
    private var currentID = 1000
    private val theatreSeating: Map<Int, MutableList<String>> =
        mapOf(
            1 to mutableListOf(
                "A1", "A2", "A3", "A4", "A5",
                "B1", "B2", "B3", "B4", "B5",
                "C1", "C2", "C3", "C4", "C5",
            ),
            2 to mutableListOf(
                "A1", "A2", "A3",
                "B1", "B2", "B3",
                "B4", "B5", "B6",
                "B7", "B8", "B9",
                "C1", "C2", "C3"
            ),
            3 to mutableListOf(
                "A1", "A2", "A3", "A4",
                "B1", "B2", "B3", "B4",
                "C1", "C2", "C3", "C4",
                "D1", "D2", "D3", "D4"
            ),
        )

    fun addScreening(screening: Screening) =
        if (theatreExists(screening.theatreID)) {
            screening.screeningID = getNextID()
            screening.seats = getSeats(screening.theatreID)
            screenings.add(screening)
        } else false
    
    fun getScreening(id: Int) =
        screenings.find { it.screeningID == id }

    fun getScreeningsByMovie(movieID: Int) =
        screenings.filter{ it.movie.movieID == movieID }.ifEmpty { null }

    fun getScreeningsByDate(date: LocalDate) =
        screenings.filter{ it.screeningDateTime.toLocalDate().equals(date) }.ifEmpty { null }

    private fun getSeats(theatreID: Int) =
        theatreSeating[theatreID]!!.toMutableList()

    private fun getNextID() =
        currentID++

    fun updateScreening(id: Int, screening: Screening) =
        if (screeningExists(id)) {
            val screeningToUpdate = getScreening(id)!!
            screeningToUpdate.movie = screening.movie
            screeningToUpdate.screeningDateTime = screening.screeningDateTime
            screeningToUpdate.theatreID = screening.theatreID
            screeningToUpdate.seats = theatreSeating[screening.theatreID]!!
            true
        } else false

    fun deleteScreening(id: Int) =
        if (screeningExists(id)) {
            val screening = getScreening(id)
            screenings.remove(screening)
            screening
        } else null

    fun deleteScreeningsByMovie(movieID: Int) =
        if (hasScreeningsByMovie(movieID)) {
            val screeningsToDelete = getScreeningsByMovie(movieID)!!
            for ( screening in screeningsToDelete ) {
                screenings.remove(screening)
            }
            screeningsToDelete
        } else null

    fun deleteScreeningsByDate(date: LocalDate) =
        if (hasScreeningsByDate(date)) {
            val screeningsToDelete = getScreeningsByDate(date)!!
            for ( screening in screeningsToDelete ) {
                screenings.remove(screening)
            }
            screeningsToDelete
        } else null

    fun reserveSeats(id: Int, seats: List<String>) =
        if (hasSeatsAvailable(id, seats)) {
            getScreening(id)!!.seats.removeAll(seats)
        } else false

    private fun filterScreenings(predicate: (Screening) -> (Boolean)) =
        if (hasScreenings())
            screenings
                .filter{ predicate(it) }
                .map{ it.toString() }
                .ifEmpty { null }
        else null

    fun listAllScreenings() =
        filterScreenings { true }

    fun listScreeningsByMovie(movieID: Int) =
        filterScreenings { it.movie.movieID == movieID }

    fun listScreeningsByDate(date: LocalDate) =
        filterScreenings { it.screeningDateTime.toLocalDate() == date }

    fun listScreeningsByMovieAndDate(movieID: Int, date: LocalDate) =
        filterScreenings {
            it.movie.movieID == movieID &&
            it.screeningDateTime.toLocalDate() == date
        }

    fun listDayRemainingScreenings() =
        filterScreenings{
            it.screeningDateTime.isAfter(LocalDateTime.now()) &&
            it.screeningDateTime.isBefore(
                LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.of(0, 0)
                ).plusDays(1)
            )
        }

    fun listDayRemainingScreeningsByMovie(movieID: Int) =
        filterScreenings{
            it.movie.movieID == movieID &&
            it.screeningDateTime.isAfter(LocalDateTime.now()) &&
            it.screeningDateTime.isBefore(
                LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.of(0, 0)
                ).plusDays(1)
            )
        }

    fun listWeekRemainingScreenings() =
        filterScreenings{
            it.screeningDateTime.isAfter(LocalDateTime.now()) &&
            it.screeningDateTime.isBefore(
                LocalDateTime.of(
                    LocalDate.now().with(DayOfWeek.SUNDAY),
                    LocalTime.of(0, 0)
                ).plusDays(1)
            )
        }

    fun listWeekRemainingScreeningsByMovie(movieID: Int) =
        filterScreenings{
            it.movie.movieID == movieID &&
            it.screeningDateTime.isAfter(LocalDateTime.now()) &&
            it.screeningDateTime.isBefore(
                LocalDateTime.of(
                    LocalDate.now().with(DayOfWeek.SUNDAY),
                    LocalTime.of(0, 0)
                ).plusDays(1)
            )
        }

    fun listAvailableSeats(id: Int) =
        getScreening(id)?.seats?.toMutableList()?.ifEmpty { null }

    fun numberOfScreenings() =
        screenings.size

    fun numberOfScreeningsByMovie(movieID: Int) =
        listScreeningsByMovie(movieID)?.size ?: 0

    fun numberOfScreeningsByDate(date: LocalDate) =
        listScreeningsByDate(date)?.size ?: 0

    fun numberOfScreeningsByMovieAndDate(movieID: Int, date: LocalDate) =
        listScreeningsByMovieAndDate(movieID, date)?.size ?: 0

    private fun numberOfDayRemainingScreenings() =
        listDayRemainingScreenings()?.size ?: 0

    private fun numberOfDayRemainingScreeningsByMovie(movieID: Int) =
        listDayRemainingScreeningsByMovie(movieID)?.size ?: 0

    private fun numberOfWeekRemainingScreenings() =
        listWeekRemainingScreenings()?.size ?: 0

    private fun numberOfWeekRemainingScreeningsByMovie(movieID: Int) =
        listWeekRemainingScreeningsByMovie(movieID)?.size ?: 0

    fun numberOfAvailableSeats(id: Int) =
        getScreening(id)?.seats?.size ?: 0

    fun numberOfSoldSeats(id: Int) =
        if (screeningExists(id))
            theatreSeating[getScreening(id)!!.theatreID]!!.size - getScreening(id)!!.seats.size
        else 0

    fun hasScreenings() =
        numberOfScreenings() > 0

    fun hasScreeningsByMovie(movieID: Int) =
        numberOfScreeningsByMovie(movieID) > 0

    fun hasScreeningsByDate(date: LocalDate) =
        numberOfScreeningsByDate(date) > 0

    fun hasScreeningsByMovieAndDate(movieID: Int, date: LocalDate) =
        numberOfScreeningsByMovieAndDate(movieID, date) > 0

    fun hasDayRemainingScreenings() =
        numberOfDayRemainingScreenings() > 0

    fun hasDayRemainingScreeningsByMovie(movieID: Int) =
        numberOfDayRemainingScreeningsByMovie(movieID) > 0

    fun hasWeekRemainingScreenings() =
        numberOfWeekRemainingScreenings() > 0

    fun hasWeekRemainingScreeningsByMovie(movieID: Int) =
        numberOfWeekRemainingScreeningsByMovie(movieID) > 0

    private fun hasSeatsAvailable(id: Int, seats: List<String>) =
        getScreening(id)?.seats?.containsAll(seats) ?: false

    fun screeningExists(id: Int) =
        screenings.find { it.screeningID == id } != null

    fun theatreExists(theatreID : Int) =
        theatreSeating.containsKey(theatreID)

    fun availableDateTime(datetime: LocalDateTime, runtime: Int, theatreID: Int): Boolean {
        if(
            runtime > 0 &&
            theatreExists(theatreID) &&
            datetime.toLocalTime().isAfter(openingTime) &&
            datetime.toLocalTime().isBefore(closingTime)
        ) {
            val sameDateAndTheatre =
                screenings
                    .filter {
                        it.screeningDateTime.toLocalDate() == datetime.toLocalDate() &&
                                it.theatreID == theatreID
                    }

            if (sameDateAndTheatre.isNotEmpty()) {
                val theatreTurnaroundInMinutes = 30L
                val requestedStartTime = datetime.toLocalTime()
                val requestedEndTime =
                    requestedStartTime.plusMinutes(runtime.toLong() + theatreTurnaroundInMinutes)

                for (s in sameDateAndTheatre) {
                    val bookedStartTime = s.screeningDateTime.toLocalTime()
                    val bookedEndTime = bookedStartTime.plusMinutes(
                        s.movie.runtime.toLong() + theatreTurnaroundInMinutes
                    )


                    if (
                    // 3 possible fail cases
                        (
                            // 1. Having a start time before another screening has ended
                            requestedStartTime.isAfter(bookedStartTime) &&
                            requestedStartTime.isBefore(bookedEndTime)
                        ) ||
                        (
                            // 2. Having an end time after another screening has started
                            requestedEndTime.isBefore(bookedEndTime) &&
                            requestedEndTime.isAfter(bookedStartTime)
                        ) ||
                        (
                            // 3. Starting and ending between another screening
                            requestedStartTime.isAfter(bookedStartTime) &&
                            requestedEndTime.isBefore(bookedEndTime)
                        )
                    ) return false // Clash, not available
                }
                return true // No clashes found
            } else return true // No screenings to clash with, available
        } else return false
    }
}